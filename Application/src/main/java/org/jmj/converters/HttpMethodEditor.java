package org.jmj.converters;

import org.jmj.entity.HttpMethod;
import org.yaml.snakeyaml.util.EnumUtils;

import java.beans.PropertyEditorSupport;

public class HttpMethodEditor extends PropertyEditorSupport {


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(HttpMethod.valueOf(text.toUpperCase()));
    }
}
