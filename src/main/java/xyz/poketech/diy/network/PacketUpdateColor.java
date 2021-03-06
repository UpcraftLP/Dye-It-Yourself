package xyz.poketech.diy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.poketech.diy.util.color.NBTColorUtil;

public class PacketUpdateColor implements IMessage {

    private int entityId;
    private int color;

    public PacketUpdateColor() {
    }

    public PacketUpdateColor(Entity entityIn, int color) {
        this(entityIn.getEntityId(), color);
    }

    public PacketUpdateColor(int entityId, int color) {
        this.entityId = entityId;
        this.color = color;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.color = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.color);
    }


    @SideOnly(Side.CLIENT)
    public Entity getEntity(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    @SideOnly(Side.CLIENT)
    public int getColor() {
        return this.color;
    }

    public static class ColorPacketHandler implements IMessageHandler<PacketUpdateColor, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateColor message, MessageContext ctx) {

            World world = Minecraft.getMinecraft().world;

            Minecraft.getMinecraft().addScheduledTask(() -> {
                Entity entity = world.getEntityByID(message.entityId);

                if(entity != null) {
                    int color = message.color;
                    entity.getEntityData().setInteger(NBTColorUtil.COLOR_KEY, color);
                }
            });

            return null;
        }
    }

}
