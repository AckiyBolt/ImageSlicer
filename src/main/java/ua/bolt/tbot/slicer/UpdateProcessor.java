package ua.bolt.tbot.slicer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InputFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.apache.logging.log4j.Logger;
import ua.bolt.tbot.slicer.model.CutImageResult;
import ua.bolt.tbot.util.LoggingUtil;

import java.io.File;
import java.util.List;

/**
 * Created by kbelentsov on 22.12.2015.
 */
public class UpdateProcessor {

    private static final Logger logger = LoggingUtil.getLogger(UpdateProcessor.class);
    private static final String MIME_TYPE = "application/zip";

    private TelegramBot api;
    private ImageCutter cutter;
    private Configuration config;

    public UpdateProcessor(TelegramBot api, Configuration config) {
        this.api = api;
        this.config = config;
        this.cutter = new ImageCutter();
    }

    public void processFile(Update update) {
        LoggingUtil.logProcess(update, logger);

        String updateId = String.valueOf(update.updateId());
        String updateDir = config.tempdir + updateId;

        String fileId = update.message().document().fileId();
        GetFileResponse file = api.getFile(fileId);

        processImage(update, updateId, updateDir, file);
    }

    public void processPhoto(Update update) {
        LoggingUtil.logProcess(update, logger);

        PhotoSize[] photoSizes = update.message().photo();
        PhotoSize photo = photoSizes[photoSizes.length - 1];

        String updateId = String.valueOf(update.updateId());
        String updateDir = config.tempdir + updateId;

        GetFileResponse file = api.getFile(photo.fileId());
        processImage(update, updateId, updateDir, file);
    }

    private void processImage(Update update, String updateId, String updateDir, GetFileResponse file) {
        String url = api.getFullFilePath(file.file());

        File fileOnDisc = FileUtil.getFileFromWeb(updateDir, updateId, url);

        CutImageResult cutImageResult = cutter.cutImages(fileOnDisc, updateDir);
        List<File> cutImages = cutImageResult.getFiles();

        File zip = FileUtil.writeZipFile(updateDir, updateId, cutImages);

        Integer chatId = update.message().from().id();

        api.sendMessage(chatId, "Зроблено!\nВийшло рядків: " + cutImageResult.getRowCount() + "\nОсьо архів із шматочками, до зустрічі ;)");
        api.sendDocument(chatId, new InputFile(MIME_TYPE, zip), update.message().messageId(), null);

        FileUtil.deleteDir(updateDir);
    }

    public void processHelp(Update update) {
        LoggingUtil.logProcess(update, logger);

        api.sendMessage(update.message().from().id(),
                "" +
                        "Вітаю, агенте!\n" +
                        "Я тобі допоможу порізать картинки на банери.\n" +
                        "Просто відправ мені зображення, яке хочеш зробити банером, як картинку або файлом до 10Мб =)");
    }
}
