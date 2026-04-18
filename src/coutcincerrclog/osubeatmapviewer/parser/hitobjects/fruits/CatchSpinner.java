package coutcincerrclog.osubeatmapviewer.parser.hitobjects.fruits;

import coutcincerrclog.osubeatmapviewer.parser.hitobjects.HitObject;
import coutcincerrclog.osubeatmapviewer.parser.hitobjects.generic.Spinner;
import coutcincerrclog.osubeatmapviewer.util.LegacyRandom;

import java.util.ArrayList;
import java.util.List;

public class CatchSpinner extends HitObject {

    private LegacyRandom random;
    public Spinner base;

    public List<CatchBanana> bananas;

    public CatchSpinner(Spinner raw, LegacyRandom random) {
        this.base = raw;
        this.random = random;
        this.bananas = new ArrayList<>();
    }

    public void updateCalculation() {
        float interval = base.endTime - base.time;
        while (interval > 100)
            interval /= 2;

        if (interval <= 0)
            return;
        for (float j = base.time; j <= base.endTime; j += interval) {
            bananas.add(new CatchBanana((int) j, random.next(0, 512), 0));
            random.nextUnsignedInt();
            random.nextUnsignedInt();
            random.nextUnsignedInt();
        }
    }

}
