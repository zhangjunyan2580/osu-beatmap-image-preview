package coutcincerrclog.osubeatmapviewer.parser.hitobjects.fruits;

import coutcincerrclog.osubeatmapviewer.Settings;
import coutcincerrclog.osubeatmapviewer.parser.Beatmap;
import coutcincerrclog.osubeatmapviewer.parser.hitobjects.HitObject;
import coutcincerrclog.osubeatmapviewer.parser.hitobjects.HitObjectConverter;
import coutcincerrclog.osubeatmapviewer.parser.hitobjects.generic.HitCircle;
import coutcincerrclog.osubeatmapviewer.parser.hitobjects.generic.Hold;
import coutcincerrclog.osubeatmapviewer.parser.hitobjects.generic.Slider;
import coutcincerrclog.osubeatmapviewer.parser.hitobjects.generic.Spinner;
import coutcincerrclog.osubeatmapviewer.util.LegacyRandom;
import coutcincerrclog.osubeatmapviewer.util.Vec2F;

import java.util.Collection;
import java.util.Collections;

public class CatchConverter extends HitObjectConverter {

    private Beatmap beatmap;
    private Settings settings;

    private LegacyRandom random;

    private float lastStartX;
    private int lastStartTime;
    private int currentCombo;

    public CatchConverter(Beatmap beatmap, Settings settings) {
        this.beatmap = beatmap;
        this.settings = settings;
        this.random = new LegacyRandom(1337);
        this.currentCombo = -1;
    }

    @Override
    public Collection<HitObject> convertCircle(HitCircle raw) {
        Vec2F newPos = new Vec2F(raw.pos.x, raw.pos.y);
        if (currentCombo == -1)
            currentCombo = 0;
        else if (raw.newCombo)
            currentCombo = (currentCombo + 1) % 4;

        if (settings.modEZHR == Settings.MOD_HR)
            makeHROffset(newPos, raw.time);
        return Collections.singletonList(new CatchHitCircle(raw.time, newPos.x, currentCombo));
    }

    @Override
    public Collection<HitObject> convertSlider(Slider raw) {
        return Collections.emptyList();
    }

    @Override
    public Collection<HitObject> convertSpinner(Spinner raw) {
        CatchSpinner spinner = new CatchSpinner(raw, random);
        spinner.updateCalculation();
        return Collections.unmodifiableList(spinner.bananas);
    }

    @Override
    public Collection<HitObject> convertHold(Hold raw) {
        return Collections.emptyList();
    }

    @Override
    public void postProcessing() {

    }

    private void makeHROffset(Vec2F pos, int time) {
        if (lastStartX == 0) {
            lastStartX = pos.x;
            lastStartTime = time;
            return;
        }

        float diff = lastStartX - pos.x;
        int timeDiff = time - lastStartTime;

        if (timeDiff > 1000) {
            lastStartX = pos.x;
            lastStartTime = time;
            return;
        }

        if (diff == 0) {
            boolean right = random.nextBoolean();
            float rand = Math.min(20, random.next(0, timeDiff / 4));
            if (right) {
                if (pos.x + rand <= 512)
                    pos.x += rand;
                else
                    pos.x -= rand;
            } else {
                if (pos.x - rand >= 0)
                    pos.x -= rand;
                else
                    pos.x += rand;
            }
            return;
        }

        // Intentionally this is integer division
        // noinspection IntegerDivisionInFloatingPointContext
        if (Math.abs(diff) < timeDiff / 3) {
            if (diff > 0) {
                if (pos.x - diff > 0)
                    pos.x -= diff;
            } else {
                if (pos.x - diff < 512)
                    pos.x -= diff;
            }
        }

        lastStartX = pos.x;
        lastStartTime = time;
    }

}
