package com.wffwebdemo.wffwebdemoproject.page.template.components;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.attribute.Value;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.keyboard.OnKeyUp;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Option;
import com.webfirmframework.wffweb.tag.html.html5.attribute.List;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Placeholder;
import com.webfirmframework.wffweb.tag.html.html5.formsandinputs.DataList;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;

/**
 * @author WFF
 *
 */
@SuppressWarnings("serial")
public class SuggestionSearchInput extends Div implements ServerAsyncMethod {
    
    private DataList dataList;
    private String dataListId;

    public SuggestionSearchInput(AbstractHtml base,
            AbstractAttribute... attributes) {
        super(base, attributes);

        develop();

    }

    private void develop() {

        dataListId = UUID.randomUUID().toString();
        
        new Input(this, new Placeholder("type Alice") , 
                new List(dataListId), 
                new OnKeyUp("return (event.key.length == 1 || event.key === 'Backspace');", this, "return {fieldValue:source.value};", null));
        
        
        dataList = new DataList(this,
                new Id(dataListId)) {{
                    
                new Option(this,
                        new Value("Alice")) {{
                        new NoTag(this, "Hello");
                }};
        }};

    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        // TODO do checking for event.getSourceAttribute()
        // if multiple events are handled by this method
        
        String fieldValue = (String) wffBMObject.getValue("fieldValue");

        if (fieldValue != null) {

            Set<AbstractHtml> options = new LinkedHashSet<AbstractHtml>();

            for (int i = 12345; i < 12345 + 100; i++) {

                final String value = "Alice " + i;

                if (value.contains(fieldValue)) {
                    
                    Option option = new Option(this, new Id(dataListId),
                            new Value(value)) {
                        {
                            new NoTag(this, "Hello "+value);
                        }
                    };
                    
                    options.add(option);
                }

            }
            
            // will be available since wffweb-2.1.3
//            dataList.addInnerHtmls(options.toArray(new AbstractHtml[options.size()]));

            dataList.removeAllChildren();
            dataList.appendChildren(options);
        }

        return null;
    }

}
