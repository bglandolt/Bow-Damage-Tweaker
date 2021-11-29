package bowdamagetweaker.mod.handler;

import bowdamagetweaker.mod.util.ConfigurationHandler;
import bowdamagetweaker.mod.util.ConfigurationHandler.SpartanBow;
import bowdamagetweaker.mod.util.Sounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlers
{
    public static final EventHandlers INSTANCE = new EventHandlers();
    
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
		Entity entity = event.getEntity();
		World world = event.getWorld();
		
		if ( world == null || entity == null )
		{
			return;
		}
		
		if ( entity instanceof EntityArrow )
		{
			EntityArrow arrow = (EntityArrow)entity;
			if ( arrow.shootingEntity instanceof EntityLivingBase )
			{
				EntityLivingBase p = (EntityLivingBase) arrow.shootingEntity;
				try
				{
					Item item = p.getHeldItem(p.getActiveHand()).getItem();
					if ( item instanceof ItemBow )
					{
						System.out.println(item);

						String name = item.getRegistryName().toString();

						for ( SpartanBow spartanBow : ConfigurationHandler.bows )
						{
							if ( name.equals(spartanBow.bow) )
							{
								arrow.setDamage(arrow.getDamage()+spartanBow.damage);
								return;
							}
						}
					}
				}
				catch ( Exception e )
				{
					
				}
			}
		}
	}

    @SubscribeEvent(priority = EventPriority.LOWEST)
   	public void arrowImpact(ProjectileImpactEvent event)
   	{
   		if ( event == null || !(event.getEntity() instanceof EntityArrow) || event.getEntity() == event.getRayTraceResult().entityHit )
   		{
   			return;
   		}
   		Entity entity = event.getRayTraceResult().entityHit;
   		if ( entity instanceof EntityLivingBase )
   		{
   			if ( ConfigurationHandler.playArrowHitSound )
   			{
   				EntityArrow arrow = (EntityArrow)(event.getEntity());
   				Entity source = arrow.shootingEntity;
   				if ( source instanceof EntityPlayer )
   				{
   					if ( arrow.getIsCritical() )
   					{
   						source.world.playSound(null, source.posX, source.posY, source.posZ, Sounds.CRITICAL_STRIKE, source.getSoundCategory(), 0.5F, 1.0F + source.world.rand.nextFloat()*0.3F);
   					}
					source.world.playSound(null, source.posX, source.posY, source.posZ, Sounds.SWORD_SLASH, source.getSoundCategory(), 0.75F, 1.0F + source.world.rand.nextFloat()*0.3F);
   				}
   			}
   		}
   	}
}