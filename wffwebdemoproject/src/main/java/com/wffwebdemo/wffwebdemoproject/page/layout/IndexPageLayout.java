package com.wffwebdemo.wffwebdemoproject.page.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.H4;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.attribute.Href;
import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.Src;
import com.webfirmframework.wffweb.tag.html.attribute.Target;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.links.A;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.programming.Script;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;
import com.wffwebdemo.wffwebdemoproject.page.template.LoginTemplate;
import com.wffwebdemo.wffwebdemoproject.page.template.components.SuggestionSearchInput;

public class IndexPageLayout extends Html {

    private static final long serialVersionUID = 1L;

    private TitleTag pageTitle;

    private HttpSession httpSession;
    
    private List<Runnable> allThreads;

    private BrowserPage browserPage;

    public IndexPageLayout(HttpSession httpSession, BrowserPage browserPage) {
        super(null);
        this.httpSession = httpSession;
        this.browserPage = browserPage;
        super.setPrependDocType(true);
        
        allThreads = new ArrayList<Runnable>();
        develop();
    }

    @SuppressWarnings("serial")
    private void develop() {

        new Head(this) {
            {
                pageTitle = new TitleTag(this) {
                    {
                        new NoTag(this);
                    }
                };
                
                new Script(this, new Type(Type.TEXT_JAVASCRIPT), new Src("js/util.js"));
            }
        };

        new Body(this, new Style("background:lightgray")) {

            {
                
                new SuggestionSearchInput(this);
                new Br(this);
                new Br(this);
                
                
                //it's kept as a separate js file
                final String js = "var argument = {"
                        + "'somekey':'some value',"
                        + "'string':'string value',"
                        + "'numb':55555,"
                        + "'bool':true,"
                        + "'regex':/ab+c/,"
                        + "'anObj':{'key':'val'},"
                        + "'numberArray':[5,55,555,55,5555]"
                        + "};"
                        + "wffAsync.serverMethod('testServerMethod', argument).invoke(function(obj){"
                        + "console.log('callback obj ', obj);"
                        + "for (key in obj) {console.log('key is ' + key + ' ', obj[key]);}"
//                        + "console.log('callback ', obj.serverKey);"
//                        + "console.log('callback string ', obj.string);"
//                        + "console.log('callback nul ', obj.nul);"
//                        + "console.log('callback number ', obj.number);"
//                        + "console.log('callback undefefined ', obj.undef);"
//                        + "console.log('callback regex ', obj.reg);"
//                        + "console.log('callback bool ', obj.bool);"
//                        + "console.log('callback anotherObj ', obj.anotherObj);"
//                        + "console.log('callback stringArray ', obj.stringArray);"
//                        + "console.log('callback numberArray ', obj.numberArray);"
//                        + "console.log('callback booleanArray ', obj.booleanArray);"
//                        + "console.log('callback funcArray ', obj.funcArray);"
//                        + "console.log('callback nullArray ', obj.nullArray);"
//                        + "console.log('callback undefinedArray ', obj.undefinedArray);"
//                        + "console.log('callback arrayArray ', obj.arrayArray);"
//                        + "console.log('callback objectArray ', obj.objectArray);"
                        //obj.byteArray is an UInt8Array
                        + "console.log('callback byteArray ', obj.byteArray);"
                        //using wff utf-8 decoder
                        + "console.log('callback byteArray ' + wffGlobal.decoder.decode(obj.byteArray));"
                        + "obj.testFun('hi how are you');"
                        + "});";
                
                new Button(this, new OnClick("invokeServerMethod()")) {
                    {
                        new NoTag(this, "Custom Server Method Invocation");
                    }
                };
                
                
                new Br(this);
                new Br(this);
                new A(this, new Href("https://github.com/webfirmframework/wffweb-demo-deployment"), new Target(Target.BLANK)) {
                    {
                        new NoTag(this, "Find its code in github");
                    }
                };
                new Br(this);
                new Br(this);
                
                new A(this, new Href("server-log"), new Target(Target.BLANK)) {
                    {
                        new NoTag(this, "view server log");
                    }
                };
                
                
                new Br(this);
                new Br(this);
                
                //to print server time
                new H4(this) {
                    {
                        new NoTag(this, "Server time : ");

                        final Span timeSpan = new Span(this);

                        Runnable thread = new Runnable() {

                            @Override
                            public void run() {
                                while (!Thread.interrupted()) {
                                    try {
                                        timeSpan.addInnerHtml(new NoTag(null,
                                                new Date().toString()));
//                                        LOGGER.info(
//                                                "Server Time " + new Date());
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        break;
                                    }
                                }

                                LOGGER.info(
                                        "Server time printing thread stopped");
                            }
                        };

                        allThreads.add(thread);
                    }
                };
                
                
                
                
                new Br(this);
                new Br(this);
                
                new NoTag(this, "Username : demo");
                new Br(this);
                new NoTag(this, "Password : demo");
                
                new Br(this);
                new Br(this);
                
                DocumentModel documentModel = new DocumentModel(browserPage);
                
                Div bodyDiv = new Div(this);
                documentModel.setBodyDiv(bodyDiv);
                
                documentModel.setPageTitle(pageTitle);
                documentModel.setHttpSession(httpSession);
                
                final LoginTemplate loginTemplate = new LoginTemplate(documentModel);
                bodyDiv.appendChild(loginTemplate);
                final Div anotherDiv = new Div(this, new Name("insertBeforeThis"));
                anotherDiv.appendChild(new Button(null, new OnClick(new ServerAsyncMethod() {
                    
                    private int count = 0;
                    
                    @Override
                    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {
                        count++;
                        anotherDiv.insertBefore(
                                new NoTag(null, "Hello Test User " + count));
                        // TODO Auto-generated method stub
                        return null;
                    }
                })){
                    {
                        new NoTag(this, "Click To Change");
                    }
                });
            }

        };

    }
    
    public List<Runnable> getAllThreads() {
        return allThreads;
    }

}
