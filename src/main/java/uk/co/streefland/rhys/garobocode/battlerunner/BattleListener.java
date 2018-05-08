package uk.co.streefland.rhys.garobocode.battlerunner;

import robocode.BattleResults;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;

/**
 * This class serves only to extract the battle results from the Robocode engine
 */
public class BattleListener extends BattleAdaptor {

    private BattleResults[] results;

    @Override
    public void onBattleCompleted(BattleCompletedEvent event) {
        results = event.getIndexedResults();
    }

    public BattleResults[] getResults() {
        return results;
    }
}
