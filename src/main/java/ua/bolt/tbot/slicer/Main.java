package ua.bolt.tbot.slicer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.yaml.snakeyaml.Yaml;
import retrofit.RetrofitError;

import java.util.List;

/**
 * Created by kbelentsov on 22.12.2015.
 */
public class Main {

    private static final String CONFIG_FILE = "config.yaml";
    private static int offset;

    public static void main(String[] args) throws Exception {

        Configuration config = loadConfiguration();

        TelegramBot bot = TelegramBotAdapter.build(config.api.token);
        UpdateProcessor processor = new UpdateProcessor(bot, config);

        while(true) {
            try {
                GetUpdatesResponse upd = bot.getUpdates(offset, config.api.limit, config.api.timeout);
                List<Update> updates = upd.updates();

                updates.stream().forEach(System.out::println);

                updates.stream().filter(Main::isPrivate).filter(Main::isHelp).forEach(processor::processHelp);
                updates.stream().filter(Main::isPrivate).filter(Main::hasPhoto).forEach(processor::processPhoto);

                offset = updates.get(updates.size() - 1).updateId() + 1;

                Thread.sleep(config.api.period);

            } catch (RetrofitError e) {
                System.out.println("Timeout. Retry");
            }
        }
    }

    private static Configuration loadConfiguration() {
        Configuration configuration = new Yaml().loadAs(
                Main.class.getClassLoader().getResourceAsStream(CONFIG_FILE),
                Configuration.class);
        System.out.println("Configuration:\n" + configuration);
        return configuration;
    }

    private static boolean isHelp(Update update) {
        String text = update.message().text();
        return text != null && text.toLowerCase().matches("/help|/start");
    }

    private static boolean hasPhoto(Update update) {
        PhotoSize[] photo = update.message().photo();
        return photo != null && photo.length != 0;
    }

    private static boolean isPrivate(Update update) {
        return update.message().chat().type() == Chat.Type.Private;
    }
}
