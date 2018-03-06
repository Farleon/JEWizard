/*
 * Copyright (c) 2012-2017
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.sample.ide.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.eclipse.che.api.core.util.CommandLine;
import org.eclipse.che.api.core.util.ShellFactory;
import org.eclipse.che.sample.shared.dao.TechnologyDAO;

public class SamplePageViewImpl implements SamplePageView {

  private ActionDelegate delegate;
  private final DockLayoutPanel rootElement;
  private final TechnologyDAO dao;
  @UiField TextBox compilerVersion;
  @UiField ListBox technologies;
  @UiField ListBox projecttypes;

  private static SamplePageViewImplUiBinder uiBinder = GWT.create(SamplePageViewImplUiBinder.class);

  @Inject
  public SamplePageViewImpl() {
    rootElement = uiBinder.createAndBindUi(this);
    dao = TechnologyDAO.getInstance();
    technologies.setVisibleItemCount(10);
    projecttypes.setVisibleItemCount(10);
    for (String t : dao.getTechnologies().keySet()) {
      technologies.addItem(t);
    }
    technologies.addItem("testopvulling");
    technologies.addItem("testopvulling1");
    technologies.addItem("testopvulling2");
    technologies.addItem("testopvulling3");
    technologies.addItem("testopvulling4");
    CommandLine cmd = new CommandLine().add("ls", "-l", "/home/andrew/some dir");
    final String[] line = new ShellFactory.StandardLinuxShell().createShellCommand(cmd);
  }

  @UiHandler({"compilerVersion"})
  void onKeyUp(KeyUpEvent event) {
    delegate.onCompilerVersionChanged();
  }

  @UiHandler("technologies")
  void onChangeListBoxTech(ChangeEvent event) {
    int index = technologies.getSelectedIndex();
    String val = technologies.getValue(index);
    projecttypes.clear();
    for (String p : dao.getTechnologies().get(val).getProjectTypes().keySet()) {
      projecttypes.addItem(p);
    }
    projecttypes.addItem(val + "testopvulling1");
    projecttypes.addItem(index + "testopvulling2");
    projecttypes.addItem(val + "testopvulling3");
    delegate.onProjectTypeChanged();
  }

  @UiHandler("projecttypes")
  void onChangeListBoxProj(ChangeEvent event) {
    ;
    delegate.onProjectTypeChanged();
  }

  @Override
  public void setDelegate(ActionDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public Widget asWidget() {
    return rootElement;
  }

  @Override
  public String getCompilerVersion() {
    return compilerVersion.getText();
  }

  @Override
  public void setCompilerVersion(String version) {
    compilerVersion.setText(version);
  }

  public String getSelectedProjectType() {
    int index = projecttypes.getSelectedIndex();
    String val = projecttypes.getValue(index);
    return val;
  }

  interface SamplePageViewImplUiBinder extends UiBinder<DockLayoutPanel, SamplePageViewImpl> {}

  @Override
  public String getSelectedTechnology() {
    int index = technologies.getSelectedIndex();
    String val = technologies.getValue(index);
    return val;
  }
}
