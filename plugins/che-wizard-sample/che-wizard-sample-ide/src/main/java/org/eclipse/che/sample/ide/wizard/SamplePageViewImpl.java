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
import java.util.HashMap;
import org.eclipse.che.sample.shared.dao.TechnologyDAO;
import org.eclipse.che.sample.shared.logic.Technology;

public class SamplePageViewImpl implements SamplePageView {

  private ActionDelegate delegate;
  private final DockLayoutPanel rootElement;
  private final TechnologyDAO dao;
  @UiField ListBox deployGoal;
  @UiField ListBox technologies;
  @UiField ListBox projecttypes;

  private static SamplePageViewImplUiBinder uiBinder = GWT.create(SamplePageViewImplUiBinder.class);

  @Inject
  public SamplePageViewImpl() {
    rootElement = uiBinder.createAndBindUi(this);
    dao = TechnologyDAO.getInstance();
    technologies.setVisibleItemCount(10);
    projecttypes.setVisibleItemCount(10);
    deployGoal.setVisibleItemCount(10);
  }

  @UiHandler("technologies")
  void onChangeListBoxTech(ChangeEvent event) {
    int index = technologies.getSelectedIndex();
    String val = technologies.getValue(index);
    projecttypes.clear();
    for (String p : dao.getTechnologies().get(val).getProjectTypes().keySet()) {
      projecttypes.addItem(p);
    }
    delegate.onProjectTypeChanged();
  }

  @UiHandler("projecttypes")
  void onChangeListBoxProj(ChangeEvent event) {
    int index = technologies.getSelectedIndex();
    String val = technologies.getValue(index);
    index = projecttypes.getSelectedIndex();
    String val2 = projecttypes.getValue(index);
    deploygoal.clear();
    for (String p : dao.getTechnologies().get(val).getProjectTypes().get(val2).getDeployGoals()) {
      projecttypes.addItem(p);
    }
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
  public String getDeployGoal() {
    return deployGoal.getText();
  }

  @Override
  public void setDeployGoal(String version) {
    deployGoal.setText(version);
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

  @Override
  public void setTechnologies(HashMap<String, Technology> tech) {
    dao.refillDao(tech);
    for (String t : dao.getTechnologies().keySet()) {
      technologies.addItem(t);
    }
  }
}
