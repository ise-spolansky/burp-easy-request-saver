package burp;

import com.securityevaluators.burpeasyrequestsaver.DataSource;
import com.securityevaluators.burpeasyrequestsaver.DataType;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class BurpExtender implements IBurpExtender, IContextMenuFactory
{

    private PrintWriter stdout;
    private PrintWriter stderr;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // set our extension name
        callbacks.setExtensionName("Easy Request Saver");

        // obtain our output and error streams
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);

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

            item.addActionListener((arg) ->
                    SaveItems(source, type, iContextMenuInvocation.getSelectedMessages()));

            exportItem.add(item);
        }

        return Collections.singletonList(exportItem);
    }

    private void SaveItems(DataSource source, DataType type, IHttpRequestResponse[] requests) {

    }
}