package com.randomlunacy.quarrycraft2.tasks;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.randomlunacy.quarrycraft2.objects.Messages;
import com.randomlunacy.quarrycraft2.objects.Quarry;

import org.bukkit.scheduler.BukkitRunnable;

public class QuarryCleaner extends BukkitRunnable
{
    @Override
    public void run()
    {
        for (Quarry q : QuarryCraft2.getInstance().getQuarryList().getQuarries())
            if ((q.isMarkedForDeletion() || !Quarry.isQuarryLayout(q.getCentreChest())) && q.isClearedPlatform())
            {

                q.tellOwner(Messages.getQuarryDestroyed(q.getCentreChestLocation()));
                QuarryCraft2.getInstance().getQuarryList().removeQuarry(q);
                return;
            }
    }
}
