package org.vcs;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.params.ParsedHttpParameter;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.requests.HttpTransformation;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.utilities.RandomUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MyContextMenuProvider implements ContextMenuItemsProvider {
  private final JMenuItem convert2formdata;
  private final Logging logger;

  public MyContextMenuProvider() {
    this.convert2formdata = new JMenuItem("Convert");
    this.logger = EntryPoint.api.logging();
  }
  @Override
  public List<Component> provideMenuItems(ContextMenuEvent event) {
    if (event.isFromTool(ToolType.REPEATER)) {

//      HttpRequest httpRequest = event.messageEditorRequestResponse().isPresent() ? event.messageEditorRequestResponse().get().requestResponse().request() : event.selectedRequestResponses().get(0).request();
//      HttpRequest httpRequest = event.messageEditorRequestResponse().get().requestResponse().request();

      this.convert2formdata.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
          HttpRequest httpRequest = event.messageEditorRequestResponse().get().requestResponse().request();
          String boundary = EntryPoint.api.utilities().randomUtils().randomString(32, RandomUtils.CharacterSet.ASCII_LETTERS);
          String newBody = "";

          if (!httpRequest.method().equalsIgnoreCase("POST")) {
            httpRequest = httpRequest.withTransformationApplied(HttpTransformation.TOGGLE_METHOD);
          }

          for (ParsedHttpParameter param : httpRequest.parameters()) {
            newBody += String.format("--%s\nContent-Disposition: form-data; name=\"%s\"\n\n%s\n", boundary, param.name(), param.value());
          }
          newBody += "--" + boundary + "--";
//          Add these attribute if it's a file:
//          Content-Disposition: form-data; name="foo"; filename="bar"
//          Content-Type: text/plain; charset=utf-8

          httpRequest = httpRequest
                  .withUpdatedHeader("Content-Type", "multipart/form-data; boundary=" + boundary)
                  .withBody(newBody);
//
          event.messageEditorRequestResponse().get().setRequest(httpRequest);

        }
      });

      return Arrays.asList(this.convert2formdata);
    }
    return null;
  }
}
