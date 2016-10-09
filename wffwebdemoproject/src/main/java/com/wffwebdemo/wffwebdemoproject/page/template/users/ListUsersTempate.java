package com.wffwebdemo.wffwebdemoproject.page.template.users;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.formatting.B;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.StyleTag;
import com.webfirmframework.wffweb.tag.html.tables.TBody;
import com.webfirmframework.wffweb.tag.html.tables.Table;
import com.webfirmframework.wffweb.tag.html.tables.Td;
import com.webfirmframework.wffweb.tag.html.tables.Th;
import com.webfirmframework.wffweb.tag.html.tables.Tr;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.wffwebdemoproject.page.ServerLogPage;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;

@SuppressWarnings("serial")
public class ListUsersTempate extends Div implements ServerAsyncMethod {

    private static final Logger LOGGER = Logger
            .getLogger(ListUsersTempate.class.getName());

    private TBody tBody;

    private DocumentModel documentModel;

    private List<AbstractHtml> previousRows;

    private int rowCount = 0;

    private Button nextRowsButton;
    
    private Button lazyNextRowsButton;

    private Button markGreenButton;

    private Button markVioletButton;

    private Button removeColoumnStyleButton;

    private Button removeColoumnStyleOneByOneButton;

    private Style countryColumnStyle;

    public ListUsersTempate(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;

        countryColumnStyle = new Style("background:yellow;color:orange");

        develop();
    }

    private void develop() {
        new StyleTag(this) {
            {
                new NoTag(this, "table {\n    font-family: arial, sans-serif;");
                new NoTag(this, "border-collapse: collapse;\n    width: 100%;");
                new NoTag(this, "}\n\ntd, th {");
                new NoTag(this,
                        "border: 1px solid #dddddd;\n    text-align: left;");
                new NoTag(this, "padding: 8px;\n}");
                new NoTag(this,
                        "tr:nth-child(even) {\n    background-color: #dddddd;");
                new NoTag(this, "}");
            }
        };

        new Div(this) {
            {
                new NoTag(null, "Users list");
            }
        };

        new Br(this);
        new Br(this);

        nextRowsButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Next 25 rows");
                    }
                };
            }
        };
        
        new Br(this);
        new Br(this);

        lazyNextRowsButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Next 1000 rows as stream");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        markGreenButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Mark column green");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        markVioletButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Mark column violet");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        removeColoumnStyleButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this,
                                "Remove style from column all together");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        removeColoumnStyleOneByOneButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Remove style attribute from column one by one");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);
        
        new Table(this) {
            {
                tBody = new TBody(this) {
                    {
                        new Tr(this) {
                            {
                                new Th(this) {
                                    {
                                        new NoTag(this, "Company (会社)");
                                    }
                                };
                                new Th(this) {
                                    {
                                        new NoTag(this, "Contact (接触)");
                                    }
                                };
                                new Th(this) {
                                    {
                                        new NoTag(this, "Country (国)");
                                    }
                                };
                            }
                        };

                    }
                };
            }
        };
        // initially add rows
        addRows();
    }

    private void addRows() {

        List<AbstractHtml> rows = new LinkedList<AbstractHtml>();

        for (int i = 0; i < 25; i++) {

            Tr tr = new Tr(null) {
                {
                    rowCount++;

                    new Td(this) {
                        {
                            new NoTag(this, "Alfreds Futterkiste " + rowCount);
                        }
                    };
                    new Td(this) {
                        {
                            new NoTag(this, "Maria Anders " + rowCount);
                        }
                    };
                    new Td(this, countryColumnStyle) {
                        {
                            new NoTag(this, "Germany " + rowCount);
                        }
                    };
                }
            };

            rows.add(tr);
        }

        if (previousRows != null) {
            tBody.removeChildren(previousRows);
        }

        tBody.appendChildren(rows);

        previousRows = rows;

    }
    
    private void addRowsAsStream() {

        List<AbstractHtml> rows = new LinkedList<AbstractHtml>();
        if (previousRows != null) {
            tBody.removeChildren(previousRows);
        }
        
        for (int i = 0; i < 1000; i++) {

            Tr tr = new Tr(tBody) {
                {
                    rowCount++;

                    new Td(this) {
                        {
                            new NoTag(this, "Alfreds Futterkiste " + rowCount);
                        }
                    };
                    new Td(this) {
                        {
                            new NoTag(this, "Maria Anders " + rowCount);
                        }
                    };
                    new Td(this, countryColumnStyle) {
                        {
                            new NoTag(this, "Germany " + rowCount);
                        }
                    };
                }
            };

            rows.add(tr);
        }

        previousRows = rows;
    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        if (nextRowsButton.equals(event.getSourceTag())) {
            addRows();
            countryColumnStyle.addCssProperties("nextRowsButton");
            displayInServerLogPage("nextRowsButton");
        } else if (markGreenButton.equals(event.getSourceTag())) {
            LOGGER.info("Mark column green");
            countryColumnStyle.addCssProperties("background:green");
            displayInServerLogPage("Mark column green");
        } else if (markVioletButton.equals(event.getSourceTag())) {
            LOGGER.info("Mark column violet");
            countryColumnStyle.addCssProperties("background:violet");
            displayInServerLogPage("Mark column violet");
        } else if (removeColoumnStyleButton.equals(event.getSourceTag())) {
            LOGGER.info("remove style all together");
            countryColumnStyle.getCssProperties().clear();
            displayInServerLogPage("remove style all together ");
        } else if (removeColoumnStyleOneByOneButton
                .equals(event.getSourceTag())) {
            LOGGER.info("remove style attribyte one by one");

            AbstractHtml[] ownerTags = countryColumnStyle.getOwnerTags();

            for (AbstractHtml ownerTag : ownerTags) {
                ownerTag.removeAttributes(
                        countryColumnStyle.getAttributeName());
            }
            displayInServerLogPage("remove style atribyte one by one");
        } else if (lazyNextRowsButton.equals(event.getSourceTag())) {
            
            addRowsAsStream();
            
            LOGGER.info("lazyNextRowsButton");
            displayInServerLogPage("lazyNextRowsButton");
        }

        return null;
    }

    private void displayInServerLogPage(String msg) {
        Object serverLogPageInstanceId = documentModel.getHttpSession()
                .getAttribute("serverLogPageInstanceId");
        if (serverLogPageInstanceId != null) {
            ServerLogPage serverLogPage = (ServerLogPage) BrowserPageContext.INSTANCE
                    .getBrowserPage(serverLogPageInstanceId.toString());
            serverLogPage.log(msg);
        }
    }

}
