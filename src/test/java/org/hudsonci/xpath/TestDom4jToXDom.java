/*******************************************************************************
 *
 * Copyright (c) 2012 Oracle Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *
 *    Bob Foster
 *     
 *******************************************************************************/ 

package org.hudsonci.xpath;

import org.dom4j.Document;
import org.hudsonci.xpath.impl.Dom2Dom;

/**
 *
 * @author Bob Foster
 */
public class TestDom4jToXDom extends TestDocumentSupport {
  
  private static final boolean DEBUG = false;
  
  private void compareDocs(String resourceName, org.w3c.dom.Document doc, org.w3c.dom.Document xdoc) {
    boolean eq = XMLCompare.isEqual(doc, xdoc); // doc.isEqualNode(xdoc); See comments in XMLCompare
    if (DEBUG && !eq) {
      System.out.println("**Standard document "+resourceName+":");
      printStdDocument(doc);
      System.out.println("**Mapped document "+resourceName+":");
      printStdDocument(xdoc);
    }
    assertTrue(resourceName+" XDoc from dom4j same as w3c Document", eq);
  }
  
  private void checkDocs(String resourceName) {
    Document doc = getDom4jDocument(resourceName, false);
    
    org.w3c.dom.Document xdoc = null;
    try {
      xdoc = (org.w3c.dom.Document) new Dom2Dom().dom2Dom(doc, false);
      assertTrue(resourceName+" xdoc created", xdoc != null);
    } catch (XPathException e) {
      fail(resourceName+e);
    }
    
    compareDocs(resourceName, getStdDocument(resourceName, false), xdoc);
  }
  
  public void testNoNamespaceDoc() {
    checkDocs("test1.xml");
  }
  
  public void testJellyDocs() {
    checkDocs("_api.jelly");
    checkDocs("_cli.jelly");
    checkDocs("_entryForm.jelly");
    checkDocs("_new.jelly");
    checkDocs("_restart.jelly");
    checkDocs("_safeRestart.jelly");
    checkDocs("_script.jelly");
    checkDocs("_scriptText.jelly");
    checkDocs("accessDenied.jelly");
    checkDocs("actions.jelly");
    checkDocs("addUser.jelly");
    checkDocs("advanced.jelly");
    checkDocs("ajax.jelly");
    checkDocs("ajaxBuildHistory.jelly");
    checkDocs("ajaxBuildQueue.jelly");
    checkDocs("ajaxExecutors.jelly");
    checkDocs("ajaxMatrix.jelly");
    checkDocs("ajaxRows.jelly");
    checkDocs("all.jelly");
    checkDocs("artifactList.jelly");
    checkDocs("artifacts-index.jelly");
    checkDocs("askRootPassword.jelly");
    checkDocs("atom.jelly");
    checkDocs("available.jelly");
    checkDocs("badge.jelly");
    checkDocs("ballColorTd.jelly");
    checkDocs("bar.jelly");
    checkDocs("block.jelly");
    checkDocs("body.jelly");
    checkDocs("booleanRadio.jelly");
    checkDocs("build-permalink.jelly");
    checkDocs("buildCaption.jelly");
    checkDocs("buildHealth.jelly");
    checkDocs("buildLink.jelly");
    checkDocs("buildListTable.jelly");
    checkDocs("buildProgressBar.jelly");
    checkDocs("buildRangeLink.jelly");
    checkDocs("buildStatusSummary.jelly");
    checkDocs("buildTimeTrend.jelly");
    checkDocs("builds.jelly");
    checkDocs("cascadingDescriptorList.jelly");
    checkDocs("cause.jelly");
    checkDocs("causeOfDeath.jelly");
    checkDocs("cc.xml.jelly");
    checkDocs("changes.jelly");
    checkDocs("checkUpdates.jelly");
    checkDocs("checkbox.jelly");
    checkDocs("column.jelly");
    checkDocs("columnHeader.jelly");
    checkDocs("combobox.jelly");
    checkDocs("config-blockWhenDownstreamBuilding.jelly");
    checkDocs("config-blockWhenUpstreamBuilding.jelly");
    checkDocs("config-buildWrappers.jelly");
    checkDocs("config-builders.jelly");
    checkDocs("config-cleanWorkspace.jelly");
    checkDocs("config-customWorkspace.jelly");
    checkDocs("config-disableBuild.jelly");
    checkDocs("config-publishers.jelly");
    checkDocs("config-quietPeriod.jelly");
    checkDocs("config-retryCount.jelly");
    checkDocs("config-scm.jelly");
    checkDocs("config-trigger.jelly");
    checkDocs("config-upstream-pseudo-trigger.jelly");
    checkDocs("config.jelly");
    checkDocs("configure-advanced.jelly");
    checkDocs("configure-common.jelly");
    checkDocs("configure-entries.jelly");
    checkDocs("configure.jelly");
    checkDocs("configureExecutors.jelly");
    checkDocs("confirm.jelly");
    checkDocs("confirmDelete.jelly");
    checkDocs("console.jelly");
    checkDocs("consoleFull.jelly");
    checkDocs("control.jelly");
    checkDocs("custom-jnlp.jelly");
    checkDocs("delete.jelly");
    checkDocs("deleteConfirmationPanel.jelly");
    checkDocs("description.jelly");
    checkDocs("descriptionForm.jelly");
    checkDocs("descriptorList.jelly");
    checkDocs("descriptorRadioList.jelly");
    checkDocs("detail.jelly");
    checkDocs("digest.jelly");
    checkDocs("dir.jelly");
    checkDocs("disconnect.jelly");
    checkDocs("downgrade.jelly");
    checkDocs("dropdownDescriptorSelector.jelly");
    checkDocs("dropdownList.jelly");
    checkDocs("dropdownListBlock.jelly");
    checkDocs("editDescription.jelly");
    checkDocs("editTypeIcon.jelly");
    checkDocs("editableComboBox.jelly");
    checkDocs("editableComboBoxValue.jelly");
    checkDocs("editableDescription.jelly");
    checkDocs("entries.jelly");
    checkDocs("entry.jelly");
    checkDocs("enum.jelly");
    checkDocs("enumSet.jelly");
    checkDocs("error.jelly");
    checkDocs("executors.jelly");
    checkDocs("expandButton.jelly");
    checkDocs("expandableTextbox.jelly");
    checkDocs("feeds.jelly");
    checkDocs("fingerprintCheck.jelly");
    checkDocs("firstUser.jelly");
    checkDocs("floatingBox.jelly");
    checkDocs("foo.jelly");
    checkDocs("footer.jelly");
    checkDocs("form.jelly");
    checkDocs("global.jelly");
    checkDocs("hasPermission.jelly");
    checkDocs("header.jelly");
    checkDocs("help-launcher.jelly");
    checkDocs("help.jelly");
    checkDocs("helpArea.jelly");
    checkDocs("hetero-list.jelly");
    checkDocs("hetero-radio.jelly");
    checkDocs("iconSize.jelly");
    checkDocs("inProgress.jelly");
    checkDocs("index.jelly");
    checkDocs("installed.jelly");
    checkDocs("invisibleEntry.jelly");
    checkDocs("isAdmin.jelly");
    checkDocs("isAdminOrTest.jelly");
    checkDocs("jobDeleteForm.jelly");
    checkDocs("jobLink.jelly");
    checkDocs("jobpropertysummaries.jelly");
    checkDocs("label.jelly");
    checkDocs("layout.jelly");
    checkDocs("legend.jelly");
    checkDocs("levels.jelly");
    checkDocs("link.jelly");
    checkDocs("list.jelly");
    checkDocs("listScmBrowsers.jelly");
    checkDocs("load-statistics.jelly");
    checkDocs("log.jelly");
    checkDocs("logKeep.jelly");
    checkDocs("login.jelly");
    checkDocs("loginDialog.jelly");
    checkDocs("loginError.jelly");
    checkDocs("loginLink.jelly");
    checkDocs("main-panel.jelly");
    checkDocs("main.jelly");
    checkDocs("manage.jelly");
    checkDocs("markOffline.jelly");
    checkDocs("matrix.jelly");
    checkDocs("message.jelly");
    checkDocs("myViewTabs.jelly");
    checkDocs("nested.jelly");
    checkDocs("new.jelly");
    checkDocs("newInstanceDetail.jelly");
    checkDocs("newJob.jelly");
    checkDocs("newJobDetail.jelly");
    checkDocs("newView.jelly");
    checkDocs("newViewDetail.jelly");
    checkDocs("noJob.jelly");
    checkDocs("noPrincipal.jelly");
    checkDocs("noWorkspace.jelly");
    checkDocs("node.jelly");
    checkDocs("nodepropertysummaries.jelly");
    checkDocs("opensearch.xml.jelly");
    checkDocs("option.jelly");
    checkDocs("optionalBlock.jelly");
    checkDocs("optionalProperty.jelly");
    checkDocs("outline.jelly");
    checkDocs("pane.jelly");
    checkDocs("password.jelly");
    checkDocs("permalinks.jelly");
    checkDocs("prepareDatabinding.jelly");
    checkDocs("progressBar.jelly");
    checkDocs("progressiveText.jelly");
    checkDocs("project-changes.jelly");
    checkDocs("projectActionFloatingBox.jelly");
    checkDocs("projectRelationship-help.jelly");
    checkDocs("projectRelationship.jelly");
    checkDocs("projectView.jelly");
    checkDocs("projectViewNested.jelly");
    checkDocs("projectViewRow.jelly");
    checkDocs("property.jelly");
    checkDocs("propertyTable.jelly");
    checkDocs("queue.jelly");
    checkDocs("radio.jelly");
    checkDocs("radioBlock.jelly");
    checkDocs("readOnlyTextbox.jelly");
    checkDocs("rename.jelly");
    checkDocs("repeatable.jelly");
    checkDocs("repeatableDeleteButton.jelly");
    checkDocs("repeatableProperty.jelly");
    checkDocs("richtextarea.jelly");
    checkDocs("rightspace.jelly");
    checkDocs("row.jelly");
    checkDocs("rowSet.jelly");
    checkDocs("rss20.jelly");
    checkDocs("rssBar-with-iconSize.jelly");
    checkDocs("rssBar.jelly");
    checkDocs("rssHeader.jelly");
    checkDocs("scriptConsole.jelly");
    checkDocs("search-failed.jelly");
    checkDocs("searchPopup.jelly");
    checkDocs("section.jelly");
    checkDocs("select.jelly");
    checkDocs("setIconSize.jelly");
    checkDocs("side-panel.jelly");
    checkDocs("sidepanel.jelly");
    checkDocs("sidepanel2.jelly");
    checkDocs("signup.jelly");
    checkDocs("signupWithFederatedIdentity.jelly");
    checkDocs("sites.jelly");
    checkDocs("slave-agent.jnlp.jelly");
    checkDocs("slave-mode.jelly");
    checkDocs("status.jelly");
    checkDocs("submit.jelly");
    checkDocs("success.jelly");
    checkDocs("summary.jelly");
    checkDocs("svn-password.jelly");
    checkDocs("systemInfo.jelly");
    checkDocs("tab.jelly");
    checkDocs("tabBar.jelly");
    checkDocs("table.jelly");
    checkDocs("task.jelly");
    checkDocs("taskWithDialog.jelly");
    checkDocs("tasks.jelly");
    checkDocs("test-result.jelly");
    checkDocs("test1.xml");
    checkDocs("textarea.jelly");
    checkDocs("textbox.jelly");
    checkDocs("threadDump.jelly");
    checkDocs("upstream-downstream.jelly");
    checkDocs("validateButton.jelly");
    checkDocs("value.jelly");
    checkDocs("viewTabs.jelly");
    checkDocs("whoAmI.jelly");
    checkDocs("wipeOutWorkspace.jelly");
    checkDocs("wipeOutWorkspaceBlocked.jelly");
    checkDocs("yui.jelly");

  }
}
