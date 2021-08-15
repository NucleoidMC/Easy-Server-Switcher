package fr.catcore.ess1_16_5;

import eu.pb4.polymer.item.VirtualItem;
import fr.catcore.ess_common.VersionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SwitchItem extends Item implements VirtualItem {
    public SwitchItem() {
        super(new Settings().maxCount(1));
    }

    @Override
    public Text getName() {
        return new LiteralText("To 1.17 server");
    }

    @Override
    public Text getName(ItemStack stack) {
        return new LiteralText("To 1.17 server");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        VersionHandler.switchServer(user);
        return super.use(world, user, hand);
    }

    @Override
    public Item getVirtualItem() {
        return Items.CLOCK;
    }
}
