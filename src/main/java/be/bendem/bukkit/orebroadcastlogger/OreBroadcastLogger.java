package be.bendem.bukkit.orebroadcastlogger;

import be.bendem.bukkit.orebroadcast.OreBroadcastEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bendem
 */
public class OreBroadcastLogger extends JavaPlugin implements Listener {

    private final SimpleDateFormat sdf = new SimpleDateFormat("[dd-MM-YYYY HH:mm:ss]");
    private Writer writer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        File file = new File(getDataFolder(), "logs.txt");
        createLogFile(file);
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file, getConfig().getBoolean("append", true)), Charset.forName("UTF-8"));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        if(writer == null) {
            return;
        }
        try {
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void createLogFile(File file) {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onOB(OreBroadcastEvent e) {
        log(e.getSource().getName() + '(' + e.getSource().getUniqueId() + ") mined " + e.getVein().size() + ' ' + e.getBlockMined().getType().name().replace("GLOWING_REDSTONE_ORE", "REDSTONE_ORE").toLowerCase());
    }

    private void log(String log) {
        try {
            writer.write(sdf.format(new Date()) + ' ' + log + '\n');
            writer.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
