package burp;

import com.securityevaluators.burpeasyrequestsaver.DataSource;
import com.securityevaluators.burpeasyrequestsaver.DataType;
import com.securityevaluators.burpeasyrequestsaver.Util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BurpExtender implements IBurpExtender, IContextMenuFactory
{

    private PrintWriter stdout;
    private PrintWriter stderr;
    private IExtensionHelpers helpers;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // set our extension name
        callbacks.setExtensionName("Easy Request Saver");

        // obtain our output and error streams
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        helpers = callbacks.getHelpers();

        callbacks.registerContextMenuFactory(this);
        stdout.println("Loaded Easy Request Saver extension");
    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation iContextMenuInvocation) {
        int itemCount = iContextMenuInvocation.getSelectedMessages().length;
        if (itemCount < 1) return null;
        boolean plural = itemCount > 1;
        JMenuItem exportItem = new JMenu("Export Contents");
        DataSource[] menuItemSources =
                {DataSource.REQUEST, DataSource.REQUEST, DataSource.RESPONSE, DataSource.RESPONSE};
        DataType[] menuItemTypes =
                {DataType.HEADER, DataType.BODY, DataType.HEADER, DataType.BODY};

        for(int i = 0; i < menuItemSources.length; i++)
        {
            DataSource source = menuItemSources[i];
            DataType type = menuItemTypes[i];
            JMenuItem item = new JMenuItem(source.getNoun() + " " + type.getNoun(plural));

            item.addActionListener((e) ->
                    saveItems(source, type, (Component) e.getSource(), iContextMenuInvocation.getSelectedMessages()));

            exportItem.add(item);
        }

        return Collections.singletonList(exportItem);
    }

    private void saveItems(DataSource source, DataType type, Component parentComponent, IHttpRequestResponse[] requests) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Specify a destination file name" + (requests.length > 1 ? " prefix" : ""));

        int result = chooser.showSaveDialog(parentComponent);
        if (result != JFileChooser.APPROVE_OPTION) return;
        Path chosenPath = chooser.getSelectedFile().toPath();
        if (requests.length == 1) {
            saveItem(chosenPath, requests[0], source, type);
            return;
        }

        Path pathPrefix = chosenPath.getParent();
        String prefix = Util.getBaseName(chosenPath) + " - ";
        String extension = Util.getExtension(chosenPath);
        for (int i = 0; i < requests.length; i++) {
            Path curPath = pathPrefix.resolve(prefix + i + extension);
            saveItem(curPath, requests[i], source, type);
        }
    }

    private void saveItem(Path path, IHttpRequestResponse request, DataSource source, DataType type) {
        int offset = (source == DataSource.REQUEST) ?
                helpers.analyzeRequest(request).getBodyOffset() :
                helpers.analyzeResponse(request.getResponse()).getBodyOffset();
        byte[] sourceData = (source == DataSource.REQUEST) ?
                request.getRequest() :
                request.getResponse();
        byte[] data = type == DataType.HEADER ?
                Arrays.copyOf(sourceData, offset) :
                Arrays.copyOfRange(sourceData, offset, sourceData.length);
        try {
            Files.write(path, data);
        } catch (IOException e) {
            e.printStackTrace(stderr);
        }
    }
}