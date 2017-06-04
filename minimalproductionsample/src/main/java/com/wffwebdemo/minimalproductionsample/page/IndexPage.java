package com.wffwebdemo.minimalproductionsample.page;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.tag.repository.TagRepository;
import com.wffwebdemo.minimalproductionsample.page.layout.IndexPageLayout;
import com.wffwebdemo.minimalproductionsample.page.model.DocumentModel;
import com.wffwebdemo.minimalproductionsample.server.constants.ServerConstants;

public class IndexPage extends BrowserPage {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(IndexPage.class.getName());

	// this is a standard interval
	public static final int HEARTBEAT_TIME_MILLISECONDS = 25000;

	private static final int WS_RECONNECT_TIME = 1000;

	private DocumentModel documentModel;

	private IndexPageLayout indexPageLayout;

	private AbstractHtml mainDiv;

	private List<AbstractHtml> mainDivChildren;

	public IndexPage(DocumentModel documentModel) {
		this.documentModel = documentModel;
	}

	@Override
	public String webSocketUrl() {
		return ServerConstants.DOMAIN_WS_URL.concat(ServerConstants.INDEX_PAGE_WS_URI);
	}

	@Override
	public AbstractHtml render() {
		super.setWebSocketHeartbeatInterval(HEARTBEAT_TIME_MILLISECONDS);
		super.setWebSocketReconnectInterval(WS_RECONNECT_TIME);
		
		
		documentModel.setBrowserPage(this);

		indexPageLayout = new IndexPageLayout(documentModel);
		
		mainDiv = TagRepository.findTagById("mainDivId", indexPageLayout);
		
		//to remove main div and to insert "Loading..." before rendering
		if (mainDiv != null) {
			mainDivChildren = mainDiv.getChildren();
			mainDiv.addInnerHtml(new NoTag(null, "Loading..."));
		}
		
		return indexPageLayout;
	}
	
	@Override
	public int toOutputStream(OutputStream os, String charset) throws IOException {
		int outputStream = super.toOutputStream(os, charset);
		// to restore main div in the body
		// this makes the main div to be inserted via websocket communication
		if (mainDiv != null && mainDivChildren != null) {
			mainDiv.addInnerHtmls(mainDivChildren.toArray(new AbstractHtml[mainDivChildren.size()]));
		}
		return outputStream;
	}
	
	public DocumentModel getDocumentModel() {
		return documentModel;
	}
	

}
