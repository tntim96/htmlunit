/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.NamedNodeMap}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class NamedNodeMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented()
    @Alerts(FF = { "baz=blah", "foo=bar", "id=f", "name=f" },
            IE = { "CORRECT THE EXPECTATION PLEASE!!!!" },
            IE11 = { "name=f", "id=f", "baz=blah", "foo=bar" })
    public void testAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    for(var i = 0; i < f.attributes.length; i++) {\n"
            + "      alert(f.attributes[i].name + '=' + f.attributes[i].value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html); // properties order is the reverse of what I get with FF3.6
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "name", "f", "name", "f", "name", "f", "name", "f", "null" },
            IE8 = { "name", "f", "name", "f", "name", "f", "exception", "null" })
    public void testGetNamedItem_HTML() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    alert(f.attributes.getNamedItem('name').nodeName);\n"
            + "    alert(f.attributes.getNamedItem('name').nodeValue);\n"
            + "    alert(f.attributes.getNamedItem('NaMe').nodeName);\n"
            + "    alert(f.attributes.getNamedItem('nAmE').nodeValue);\n"
            + "    try {\n"
            + "      alert(f.attributes.name.nodeName);\n"
            + "      alert(f.attributes.name.nodeValue);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    try {\n"
            + "      alert(f.attributes.NaMe.nodeName);\n"
            + "      alert(f.attributes.nAmE.nodeValue);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(f.attributes.getNamedItem('notExisting'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "name", "y", "name", "y", "null", "undefined", "null" },
            IE8 = { "name", "y", "exception", "null", "undefined", "null" })
    public void testGetNamedItem_XML() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'second.xml'") + ";\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('name').nodeName);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('name').nodeValue);\n"
            + "    try {\n"
            + "      alert(doc.documentElement.attributes.name.nodeName);\n"
            + "      alert(doc.documentElement.attributes.name.nodeValue);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('NaMe'));\n"
            + "    alert(doc.documentElement.attributes.NaMe);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('nonExistent'));\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<blah name='y'></blah>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined" },
            IE8 = { "[object]", "[object]", "[object]" })
    public void unspecifiedAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.attributes.language);\n"
            + "    alert(document.body.attributes.id);\n"
            + "    alert(document.body.attributes.dir);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "div1", "" })
    public void removeNamedItem() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<div id='div1' style='background-color:#FFFFC1;'>div1</div>\n"
            + "<script>\n"
            + "  var el = document.getElementById('div1');\n"
            + "  alert(el.id);\n"
            + "  el.attributes.removeNamedItem('id');\n"
            + "  alert(el.id);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
