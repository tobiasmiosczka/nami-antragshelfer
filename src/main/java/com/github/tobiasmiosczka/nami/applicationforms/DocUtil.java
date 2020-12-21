package com.github.tobiasmiosczka.nami.applicationforms;


import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.TableFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DocUtil {

    private static final ObjectFactory FACTORY = ObjectFactory.get();

    public static Text createText(String content) {
        Text text = FACTORY.createText();
        text.setValue(content);
        return text;
    }

    public static R createR(String content) {
        R r = FACTORY.createR();
        r.getContent().add(createText(content));
        return r;
    }

    public static P createP(String content) {
        P p = FACTORY.createP();
        p.getContent().add(createR(content));
        return p;
    }

    public static Tc createTc(String content) {
        Tc tc = FACTORY.createTc();
        tc.getContent().add(createP(content));
        return tc;
    }

    public static Tr createTr(String...contents) {
        Tr tr = FACTORY.createTr();
        for (String content : contents)
            tr.getContent().add(createTc(content));
        return tr;
    }

    public static List<Tbl> findTables(List<Object> objects) {
        TableFinder tableFinder = new TableFinder();
        new TraversalUtil(objects, tableFinder);
        return tableFinder.tblList.stream()
                .map(XmlUtils::unwrap)
                .filter(o -> o instanceof Tbl)
                .map(o -> (Tbl) o)
                .collect(Collectors.toList());
    }

    public static P getTableCellP(Tbl tbl, int row, int column) {
        Tr tr = (Tr) tbl.getContent().get(row);
        Tc tc = (Tc) XmlUtils.unwrap(tr.getContent().get(column));
        return tc.getContent().stream()
                .filter(o -> o instanceof P)
                .map(o -> (P) o)
                .findFirst().orElse(null);
    }

    public static List<HeaderPart> findHeaders(WordprocessingMLPackage wordMLPackage) {
        RelationshipsPart rp = wordMLPackage.getMainDocumentPart().getRelationshipsPart();
        List<HeaderPart> result = new ArrayList<>();
        for (Relationship r : rp.getRelationships().getRelationship())
            if (r.getType().equals(Namespaces.HEADER))
                result.add((HeaderPart) rp.getPart(r));
        return result;
    }
}
