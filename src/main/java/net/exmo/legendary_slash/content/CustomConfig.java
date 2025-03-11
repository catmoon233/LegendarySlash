package net.exmo.legendary_slash.content;

import com.google.gson.JsonElement;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.events.LoadCustomArtsCostsEvent;
import net.exmo.legendary_slash.utils.ExConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.exmo.legendary_slash.utils.ExConfigHandle.listFiles;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomConfig {
    public static class addFunction{
        public addFunction add(String name, int cost){
            SlashArtsCostMap.put(name, cost);
            Legendary_slash.LOGGER.debug("load custom sa cost: {} {}", name, cost);
            return this;
        }
        public  void copyResourceToFile(String resourcePath, String targetPath) {
            // 获取Minecraft实例目录
            File mcDir = FMLPaths.GAMEDIR.get().toFile();

            // 构建目标目录
            File targetDir = new File(mcDir, targetPath).getParentFile();

            // 创建目录
            if (!targetDir.exists() && !targetDir.mkdirs()) {
                throw new IllegalStateException("Could not create directory: " + targetDir.getAbsolutePath());
            }

            // 构建目标文件
            File targetFile = new File(targetDir, new File(targetPath).getName());

            // 使用Java NIO来复制文件，简化代码
            try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
                if (in == null) {
                    throw new IOException("Resource not found: " + resourcePath);
                }
                Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy file from " + resourcePath + " to " + targetFile, e);
            }
        }

    }
    public static Map<String, Integer> SlashArtsCostMap ;


    public static void init(){
        SlashArtsCostMap = new HashMap<>();
        MinecraftForge.EVENT_BUS.post(new LoadCustomArtsCostsEvent(new addFunction()));
    }
    @Mod.EventBusSubscriber
    public static class commonEvent{
        public static final String Config_path = FMLPaths.CONFIGDIR.get().resolve("exmo/sa_power/").toString();
        public static List<ExConfig> Foundmoconfigs = new ArrayList<>();
        @SubscribeEvent
        public static void onReload(AddReloadListenerEvent event){
            init();
        }
        @SubscribeEvent
        public static void LoadFromConfig(LoadCustomArtsCostsEvent event) throws IOException {
            Foundmoconfigs = new ArrayList<>();
            Path path = Path.of(Config_path);
            if (!new File(FMLPaths.GAMEDIR.get().resolve(path).toString()).exists()){
                event.addFunction.copyResourceToFile("/data/legendary_slash/default.json","config/exmo/sa_power/default.json");
            }
            Foundmoconfigs =  listFiles(path);
            addFunction addFunction = event.addFunction;
            for (ExConfig exConfig : Foundmoconfigs) {
                processSAPower(exConfig, addFunction);
            }

        }

        private static void processSAPower(ExConfig exConfig, addFunction addFunction) {
            try {
                for (Map.Entry<String, JsonElement> entry : exConfig.readEntrys()) {
                    addFunction.add(entry.getKey(),entry.getValue().getAsInt());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
