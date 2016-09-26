package com.wffwebdemo.wffwebdemoproject.page.template.users;

import java.util.LinkedList;
import java.util.List;

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
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;

@SuppressWarnings("serial")
public class ListUsersTempate extends Div implements ServerAsyncMethod {

    private TBody tBody;

    @SuppressWarnings("unused")
    private DocumentModel documentModel;

    private List<AbstractHtml> previousRows;

    private int rowCount = 0;

    private Button nextRowsButton;

    private Button markGreenButton;

    private Button markVioletButton;

    private Button removeColoumnStyleButton;

    private Style countryColumnStyle;

    public ListUsersTempate(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;

        countryColumnStyle = new Style("background:yellow");

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

        new Br(this);
        new Br(this);

        nextRowsButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Next");
                    }
                };
            }
        };

        markGreenButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Mark column green");
                    }
                };
            }
        };

        markVioletButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Mark column violet");
                    }
                };
            }
        };

        removeColoumnStyleButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Remove style from column");
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

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        if (nextRowsButton.equals(event.getSourceTag())) {
            addRows();
        } else if (markGreenButton.equals(event.getSourceTag())) {
            System.out.println("Mark column green");
            countryColumnStyle.addCssProperties("background:green");
        } else if (markVioletButton.equals(event.getSourceTag())) {
            System.out.println("Mark column violet");
            countryColumnStyle.addCssProperties("background:violet");
        } else if (removeColoumnStyleButton.equals(event.getSourceTag())) {
            System.out.println("remove style");
            AbstractHtml[] ownerTags = countryColumnStyle.getOwnerTags();
            
            for (AbstractHtml ownerTag : ownerTags) {
                ownerTag.removeAttributes(countryColumnStyle.getAttributeName());
            }
        }

        return null;
    }

}
