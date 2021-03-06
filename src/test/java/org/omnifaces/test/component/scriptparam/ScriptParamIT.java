/*
 * Copyright 2020 OmniFaces
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.test.component.scriptparam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.omnifaces.test.OmniFacesIT;
import org.omnifaces.util.Utils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ScriptParamIT extends OmniFacesIT {

	@FindBy(id="pageLoadTimestamp")
	private WebElement pageLoadTimestamp;

	@FindBy(id="scriptLoadTimestamp")
	private WebElement scriptLoadTimestamp;

	@FindBy(id="clientTimeZoneOffset")
	private WebElement clientTimeZoneOffset;

	@FindBy(id="appName")
	private WebElement appName;

	@Deployment(testable=false)
	public static WebArchive createDeployment() {
		return createWebArchive(ScriptParamIT.class);
	}

	@Test
	public void testScriptParam() {
		waitUntilTextContent(this.scriptLoadTimestamp);

		long pageLoadTimestamp = Long.valueOf(this.pageLoadTimestamp.getText());
		long scriptLoadTimestamp = Long.valueOf(this.scriptLoadTimestamp.getText());
		assertTrue("Script param is set later", scriptLoadTimestamp > pageLoadTimestamp);

		String clientTimeZoneOffset = this.clientTimeZoneOffset.getText();
		assertTrue("Client time zone offset is a number", Utils.isNumber(clientTimeZoneOffset));

		String appName = this.appName.getText();
		assertEquals("navigator.appName is 'Netscape'", "Netscape", appName);
	}

}