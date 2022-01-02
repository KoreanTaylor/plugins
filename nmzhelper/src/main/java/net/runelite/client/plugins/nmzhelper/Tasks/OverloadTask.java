package net.runelite.client.plugins.nmzhelper.Tasks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.nmzhelper.*;

public class OverloadTask extends Task {
    public OverloadTask(NMZHelperPlugin plugin, Client client, ClientThread clientThread, NMZHelperConfig config) {
        super(plugin, client, clientThread, config);
    }

    @Override
    public boolean validate() {
        //fail if:

        //not in the nightmare zone
        if (!MiscUtils.isInNightmareZone(client))
            return false;
        //already overloaded
        //if (client.getVar(NMZ_OVERLOAD) != 0)
        if (client.getVarbitValue(3955) != 0)
            return false;

        //don't have overloads
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

        if (inventoryWidget == null) {
            return false;
        }

        if (inventoryWidget.getWidgetItems()
                .stream()
                .filter(item -> Arrays.asList(ItemID.OVERLOAD_1, ItemID.OVERLOAD_2,
                        ItemID.OVERLOAD_3, ItemID.OVERLOAD_4).contains(item.getId()))
                .collect(Collectors.toList())
                .isEmpty())
            return false;

        //less than 50 hp
        return client.getBoostedSkillLevel(Skill.HITPOINTS) > 50;
    }

    @Override
    public String getTaskDescription() {
        return "Drinking Overload";
    }

    @Override
    public void onGameTick(GameTick event) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

        if (inventoryWidget == null) {
            return;
        }

        List<WidgetItem> items = inventoryWidget.getWidgetItems()
                .stream()
                .filter(item -> Arrays.asList(ItemID.OVERLOAD_1, ItemID.OVERLOAD_2, ItemID.OVERLOAD_3, ItemID.OVERLOAD_4).contains(item.getId()))
                .collect(Collectors.toList());

        if (items == null || items.isEmpty()) {
            return;
        }

        WidgetItem item = items.get(0);

        if (item == null)
            return;

        LegacyMenuEntry entry = MiscUtils.getConsumableEntry("", item.getId(), item.getIndex());
        clientThread.invoke(() -> client.invokeMenuAction(entry.getOption(), entry.getTarget(), entry.getIdentifier(), entry.getOpcode(), entry.getParam0(), entry.getParam1()));
        //click();
    }
}
