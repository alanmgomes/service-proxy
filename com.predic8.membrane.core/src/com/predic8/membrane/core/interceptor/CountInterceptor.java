/* Copyright 2009 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */


package com.predic8.membrane.core.interceptor;

import static com.predic8.membrane.core.util.HttpUtil.createResponse;

import java.io.StringWriter;
import java.net.*;

import javax.xml.stream.*;

import org.apache.commons.logging.*;

import com.googlecode.jatl.Html;
import com.predic8.membrane.core.exchange.Exchange;

public class CountInterceptor extends AbstractInterceptor {

	private static Log log = LogFactory.getLog(CountInterceptor.class.getName());

	private int counter;
	
	public CountInterceptor() {
		name = "Count Interceptor";		
	}
	
	@Override
	public Outcome handleRequest(Exchange exc) throws Exception {
		log.info(""+ (++counter) +". request received.");
		exc.setResponse(createResponse(200, "OK", getPage(), "text/html;charset=utf-8"));
		return Outcome.ABORT;
	}

	private String getPage() throws UnknownHostException {
		StringWriter writer = new StringWriter();
		new Html(writer) {{
				html();
					head();
						title().text(name).end();
					end();
					body();
						h1().text(name+"("+InetAddress.getLocalHost().getHostAddress()+")").end();
						p().text("This site is generated by a simple interceptor that counts how often this site was requested.").end();
						p().text("Request count: "+counter).end();
				endAll(); 
				done();	
			}
		};
		return writer.toString();
	}
	
	@Override
	protected void writeInterceptor(XMLStreamWriter out)
			throws XMLStreamException {
		
		out.writeStartElement("counter");
		
		out.writeAttribute("name", name);		
		
		out.writeEndElement();
	}
	
	@Override
	protected void parseAttributes(XMLStreamReader token) {
		
		name = token.getAttributeValue("", "name");
	}	
}