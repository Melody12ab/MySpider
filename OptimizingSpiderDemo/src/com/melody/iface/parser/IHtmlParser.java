package com.melody.iface.parser;

public interface IHtmlParser<T> {
    T parser(String htmlSource);
}
