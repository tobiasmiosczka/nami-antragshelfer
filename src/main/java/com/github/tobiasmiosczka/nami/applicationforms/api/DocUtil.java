package com.github.tobiasmiosczka.nami.applicationforms.api;

import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.TableFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DocUtil {

    protected static final ObjectFactory FACTORY = ObjectFactory.get();

    protected static Text createText(String content) {
        Text text = FACTORY.createText();
        text.setValue(content);
        return text;
    }

    protected static R createR(String content) {
        R r = FACTORY.createR();
        r.getContent().add(createText(content));
        return r;
    }

    protected static P createP(String content) {
        P p = FACTORY.createP();
        p.getContent().add(createR(content));
        return p;
    }

    protected static Tc createTc(String content) {
        Tc tc = FACTORY.createTc();
        tc.getContent().add(createP(content));
        return tc;
    }

    protected static Tr createTr(String...contents) {
        Tr tr = FACTORY.createTr();
        for (String content : contents)
            tr.getContent().add(createTc(content));
        return tr;
    }

    protected static List<Tbl> findTables(List<Object> objects) {
        TableFinder tableFinder = new TableFinder();
        new TraversalUtil(objects, tableFinder);
        return tableFinder.tblList.stream()
                .map(XmlUtils::unwrap)
                .filter(o -> o instanceof Tbl)
                .map(o -> (Tbl) o)
                .collect(Collectors.toList());
    }

    protected static P getTableCellP(Tbl tbl, int row, int column) {
        Tr tr = (Tr) tbl.getContent().get(row);
        Tc tc = (Tc) XmlUtils.unwrap(tr.getContent().get(column));
        return tc.getContent().stream()
                .filter(o -> o instanceof P)
                .map(o -> (P) o)
                .findFirst()
                .orElse(null);
    }

    protected static List<HeaderPart> findHeaders(WordprocessingMLPackage wordMLPackage) {
        RelationshipsPart rp = wordMLPackage.getMainDocumentPart().getRelationshipsPart();
        List<HeaderPart> result = new ArrayList<>();
        for (Relationship r : rp.getRelationships().getRelationship())
            if (r.getType().equals(Namespaces.HEADER))
                result.add((HeaderPart) rp.getPart(r));
        return result;
    }

    protected static void addBorders(Tbl table, int size) {
        table.setTblPr(new TblPr());
        CTBorder border = new CTBorder();
        border.setColor("auto");
        border.setSz(new BigInteger(String.valueOf(size)));
        border.setSpace(new BigInteger("0"));
        border.setVal(STBorder.SINGLE);

        TblBorders borders = new TblBorders();
        borders.setBottom(border);
        borders.setLeft(border);
        borders.setRight(border);
        borders.setTop(border);
        borders.setInsideH(border);
        borders.setInsideV(border);
        table.getTblPr().setTblBorders(borders);
    }
}
