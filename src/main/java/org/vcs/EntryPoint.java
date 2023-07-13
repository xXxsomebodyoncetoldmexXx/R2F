package org.vcs;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class EntryPoint implements BurpExtension {
  public static MontoyaApi api;
  private final String extensionName = "Request to form-data";
  @Override
  public void initialize(MontoyaApi api) {
    this.api = api;
    api.extension().setName(this.extensionName);

    api.userInterface().registerContextMenuItemsProvider(new MyContextMenuProvider());
  }
}
