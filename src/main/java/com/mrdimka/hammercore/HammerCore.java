package com.mrdimka.hammercore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.Level;

import com.mrdimka.hammercore.annotations.MCFBus;
import com.mrdimka.hammercore.api.HammerCoreAPI;
import com.mrdimka.hammercore.api.IHammerCoreAPI;
import com.mrdimka.hammercore.api.IUpdatable;
import com.mrdimka.hammercore.api.RequiredDeps;
import com.mrdimka.hammercore.api.WrappedFMLLog;
import com.mrdimka.hammercore.api.mhb.IRayRegistry;
import com.mrdimka.hammercore.api.mhb.RaytracePlugin;
import com.mrdimka.hammercore.command.CommandPosToLong;
import com.mrdimka.hammercore.command.CommandTPX;
import com.mrdimka.hammercore.common.capabilities.CapabilityEJ;
import com.mrdimka.hammercore.common.utils.AnnotatedInstanceUtil;
import com.mrdimka.hammercore.common.utils.HammerCoreUtils;
import com.mrdimka.hammercore.common.utils.WrappedLog;
import com.mrdimka.hammercore.event.AddCalculatronRecipeEvent;
import com.mrdimka.hammercore.event.GetAllRequiredApisEvent;
import com.mrdimka.hammercore.ext.TeslaAPI;
import com.mrdimka.hammercore.fluiddict.FluidDictionary;
import com.mrdimka.hammercore.gui.GuiManager;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.proxy.AudioProxy_Common;
import com.mrdimka.hammercore.proxy.ParticleProxy_Common;
import com.mrdimka.hammercore.proxy.RenderProxy_Common;
import com.pengu.hammercore.cfg.ConfigHolder;
import com.pengu.hammercore.cfg.HCModConfigurations;
import com.pengu.hammercore.cfg.IConfigReloadListener;
import com.pengu.hammercore.init.BlocksHC;
import com.pengu.hammercore.init.ItemsHC;
import com.pengu.hammercore.init.RecipesHC;
import com.pengu.hammercore.init.SimpleRegistration;

/**
 * The core of Hammer Core. <br>
 * <span style="text-decoration: underline;">
 * <em>This really sounds weird :/</em></span>
 **/
@Mod(modid = "hammercore", version = "@VERSION@", name = "Hammer Core", guiFactory = "com.pengu.hammercore.cfg.gui.GuiConfigFactory")
public class HammerCore
{
	public static final List<IUpdatable> updatables = new ArrayList<>(4);
	public static final boolean IS_OBFUSCATED_MC = false;
	
	/**
	 * Render proxy for HC used to handle complicated rendering codes in a
	 * simple way.
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.mrdimka.hammercore.proxy.RenderProxy_Client", serverSide = "com.mrdimka.hammercore.proxy.RenderProxy_Common")
	public static RenderProxy_Common renderProxy;
	
	// /**
	// * All sources compiled from 'javacode' dir
	// */
	// public static ClassLoader javaLoader;
	
	/**
	 * Audio proxy for HC used to interact with audio in any way
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.mrdimka.hammercore.proxy.AudioProxy_Client", serverSide = "com.mrdimka.hammercore.proxy.AudioProxy_Common")
	public static AudioProxy_Common audioProxy;
	
	/**
	 * Particle proxy for HC used to interact with particles from both sides.
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.mrdimka.hammercore.proxy.ParticleProxy_Client", serverSide = "com.mrdimka.hammercore.proxy.ParticleProxy_Common")
	public static ParticleProxy_Common particleProxy;
	
	/**
	 * An instance of {@link HammerCore} class
	 **/
	@Instance("hammercore")
	public static HammerCore instance;
	
	/** Creative tab of HammerCore */
	public static final CreativeTabs tab = HammerCoreUtils.createDynamicCreativeTab("hammercore", 150);
	
	public static final Map<IHammerCoreAPI, HammerCoreAPI> APIS = new HashMap<>();
	
	public static final WrappedLog LOG = new WrappedLog("Hammer Core");
	
	private List<IRayRegistry> raytracePlugins;
	private List<ConfigHolder> configListeners;
	
	/**
	 * This method is used to construct proxies
	 */
	@EventHandler
	public void construct(FMLConstructionEvent e)
	{
		renderProxy.construct();
		audioProxy.construct();
		
		if(!FluidRegistry.isUniversalBucketEnabled())
			FluidRegistry.enableUniversalBucket();
		
		new FluidDictionary();
		
		// File javacode = new File(".", "javacode");
		// if(!javacode.isDirectory()) javacode.mkdir();
		// try
		// {
		// Map<String, byte[]> classes = JavaCodeLoader.compileRoot(javacode);
		// javaLoader = JavaCodeLoader.toLoader(classes);
		// for(String clas : classes.keySet())
		// {
		// try
		// {
		// // GameRegistry.makeItemStack(itemName, meta, stackSize, nbtString)
		// Class cls = javaLoader.loadClass(clas);
		// IJavaCode code = null;
		// if(IJavaCode.class.isAssignableFrom(cls)) code = (IJavaCode)
		// cls.newInstance();
		// else code = new IJavaCode.IJavaCode_IMPL(cls.newInstance());
		// COMPILED_CODES.add(code);
		// LOG.info("Added new JavaCode: " + code + " for " + clas);
		// }
		// catch(ClassNotFoundException cnfe)
		// {
		// LOG.error("Error: unexpected class " + clas +
		// ". Perharps it has different package?");
		// }
		// catch(Throwable err) { err.printStackTrace(); }
		// }
		// } catch(Exception e1) { e1.printStackTrace(); }
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		CapabilityEJ.register();
		
		List<IHammerCoreAPI> apis = AnnotatedInstanceUtil.getInstances(e.getAsmData(), HammerCoreAPI.class, IHammerCoreAPI.class);
		List<Object> toRegister = AnnotatedInstanceUtil.getInstances(e.getAsmData(), MCFBus.class, Object.class);
		raytracePlugins = AnnotatedInstanceUtil.getInstances(e.getAsmData(), RaytracePlugin.class, IRayRegistry.class);
		
		List<IConfigReloadListener> listeners = AnnotatedInstanceUtil.getInstances(e.getAsmData(), HCModConfigurations.class, IConfigReloadListener.class);
		configListeners = new ArrayList<>();
		int i = 0;
		for(IConfigReloadListener listener : listeners)
		{
			i++;
			ConfigHolder h = new ConfigHolder(listener, new Configuration(listener.getSuggestedConfigurationFile()));
			h.reload();
			configListeners.add(h);
			LOG.info("Added \"" + h.getClass().getName() + "\" to Hammer Core Simple Configs.");
		}
		
		for(Object o : toRegister)
		{
			MinecraftForge.EVENT_BUS.register(o);
			FMLLog.log("Hammer Core", Level.INFO, "Added \"" + o + "\" to MCF Event Bus.");
		}
		
		FMLLog.log("Hammer Core", Level.INFO, "Added " + toRegister.size() + " object to MCF Event Bus.");
		
		{
			GetAllRequiredApisEvent evt = new GetAllRequiredApisEvent();
			MinecraftForge.EVENT_BUS.post(evt);
			RequiredDeps.addRequests(evt);
		}
		
		for(IHammerCoreAPI api : apis)
		{
			HammerCoreAPI apia = api.getClass().getAnnotation(HammerCoreAPI.class);
			if(apia != null)
			{
				WrappedFMLLog log = new WrappedFMLLog(apia.name());
				api.init(log, apia.version());
				APIS.put(api, apia);
			}
		}
		
		SimpleRegistration.registerFieldBlocksFrom(BlocksHC.class, "hammercore", tab);
		SimpleRegistration.registerFieldItemsFrom(ItemsHC.class, "hammercore", tab);
		
		OreDictionary.registerOre("gearIron", ItemsHC.IRON_GEAR);
		
		ModMetadata meta = e.getModMetadata();
		meta.autogenerated = false;
		meta.version = "@VERSION@";
		meta.authorList = Arrays.asList("APengu");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		renderProxy.init();
		HCNetwork.clinit();
		RecipesHC.load();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiManager());
		
		if(ItemsHC.calculatron != null)
		{
			AddCalculatronRecipeEvent evt = new AddCalculatronRecipeEvent();
			evt.setRecipe(new ShapedOreRecipe(ItemsHC.calculatron, "igi", "rlr", "idi", 'g', "blockGlass", 'i', "ingotIron", 'r', "dustRedstone", 'd', "ingotGold", 'l', "dyeLime"));
			if(!MinecraftForge.EVENT_BUS.post(evt))
				GameRegistry.addRecipe(evt.getRecipe());
		}
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CommandPosToLong());
		e.registerServerCommand(new CommandTPX());
		
		File hc_recipes_global = new File("hc-recipes");
		MinecraftServer server = e.getServer();
		File worldFolder = new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), "hc-recipes");
		worldFolder.mkdirs();
		hc_recipes_global.mkdirs();
		
		reloadRaytracePlugins();
	}
	
	@SubscribeEvent
	public void serverTick(ServerTickEvent evt)
	{
		if(evt.side == Side.SERVER)
		{
			for(int i = 0; i < updatables.size(); ++i)
			{
				try
				{
					IUpdatable upd = updatables.get(i);
					upd.update();
					if(!upd.isAlive())
						updatables.remove(i);
				} catch(Throwable err)
				{
				}
			}
		}
	}
	
	public void reloadRaytracePlugins()
	{
		TeslaAPI.refreshTeslaClassData();
		
		RayCubeRegistry.instance.cubes.clear();
		RayCubeRegistry.instance.mgrs.clear();
		for(IRayRegistry reg : raytracePlugins)
		{
			LOG.info("Registering raytrace plugin: " + reg.getClass().getName() + " ...");
			long start = System.currentTimeMillis();
			reg.registerCubes(RayCubeRegistry.instance);
			LOG.info("Registered raytrace  plugin: " + reg.getClass().getName() + " in " + (System.currentTimeMillis() - start) + " ms");
		}
	}
}