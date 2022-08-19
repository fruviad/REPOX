/*
 * MarkRemover.java
 *
 * Created on 14 de Janeiro de 2003, 11:40
 */

package pt.utl.ist.util.marc;

import org.w3c.dom.Document;

import pt.utl.ist.marc.MarcField;
import pt.utl.ist.marc.MarcRecord;
import pt.utl.ist.marc.MarcSubfield;
import pt.utl.ist.marc.xml.DomBuilder;
import pt.utl.ist.marc.xml.RecordBuilderFromMarcXml;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Nuno Freire
 */
public class RemovePontuationFromAuthorityVisitor {
    private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(RemovePontuationVisitor.class);

    /** RemovePontuationFromAuthorityVisitor fields */
    protected static HashMap               fields;
    static {
        fields = new HashMap(10);
        RemoverDefinition d = new RemoverDefinition("200");
        d.addIn("a", "\\s*,\\s*$");
        d.addIn("b", "\\s*,\\s*$");
        d.addIn("c", "\\s*,\\s*$");
        d.addIn("d", "\\s*,\\s*$");
        fields.put(d.field, d);
        d = new RemoverDefinition("400");
        d.addIn("a", "\\s*,\\s*$");
        d.addIn("b", "\\s*,\\s*$");
        d.addIn("c", "\\s*,\\s*$");
        d.addIn("d", "\\s*,\\s*$");
        fields.put(d.field, d);
        d = new RemoverDefinition("500");
        d.addIn("a", "\\s*,\\s*$");
        d.addIn("b", "\\s*,\\s*$");
        d.addIn("c", "\\s*,\\s*$");
        d.addIn("d", "\\s*,\\s*$");
        fields.put(d.field, d);

        d = new RemoverDefinition("210");
        d.addBefore("b", "\\s*,\\s*$");
        d.addBefore("c", "\\s*,\\s*$");
        d.addBefore("d", "\\s*,\\s*$");
        d.addBefore("e", "\\s*,\\s*$");
        d.addBefore("f", "\\s*,\\s*$");
        d.addIn("b", "\\s*,\\s*$");
        d.addIn("c", "\\s*,\\s*$");
        d.addIn("d", "\\s*,\\s*$");
        d.addIn("e", "\\s*,\\s*$");
        d.addIn("a", "\\s*\\.\\s*$");
        d.addIn("b", "\\s*\\.\\s*$");
        fields.put(d.field, d);
        d = new RemoverDefinition("410");
        d.addBefore("b", "\\s*,\\s*$");
        d.addBefore("c", "\\s*,\\s*$");
        d.addBefore("d", "\\s*,\\s*$");
        d.addBefore("e", "\\s*,\\s*$");
        d.addBefore("f", "\\s*,\\s*$");
        d.addIn("b", "\\s*,\\s*$");
        d.addIn("c", "\\s*,\\s*$");
        d.addIn("d", "\\s*,\\s*$");
        d.addIn("e", "\\s*,\\s*$");
        d.addIn("a", "\\s*\\.\\s*$");
        d.addIn("b", "\\s*\\.\\s*$");
        fields.put(d.field, d);
        d = new RemoverDefinition("510");
        d.addBefore("b", "\\s*,\\s*$");
        d.addBefore("c", "\\s*,\\s*$");
        d.addBefore("d", "\\s*,\\s*$");
        d.addBefore("e", "\\s*,\\s*$");
        d.addBefore("f", "\\s*,\\s*$");
        d.addIn("b", "\\s*,\\s*$");
        d.addIn("c", "\\s*,\\s*$");
        d.addIn("d", "\\s*,\\s*$");
        d.addIn("e", "\\s*,\\s*$");
        d.addIn("a", "\\s*\\.\\s*$");
        d.addIn("b", "\\s*\\.\\s*$");
        fields.put(d.field, d);
    }

    /**
     * @param rec
     */
    public static void removePontuation(MarcRecord rec) {
        for (Object o : rec.getFields()) {
            MarcField fld = (MarcField)o;
            RemoverDefinition rd = (RemoverDefinition)fields.get(fld.getTagAsString());
            if (rd != null) {
                removePontuation(fld, rd);
            }
        }
    }

    /**
     * @param doc
     * @return Document
     */
    public static Document removePontuation(Document doc) {
        MarcRecord rec = new RecordBuilderFromMarcXml().parseDom(doc);
        removePontuation(rec);
        return DomBuilder.record2Dom(rec);
    }

    /**
     * @param fld
     * @param rd
     */
    protected static void removePontuation(MarcField fld, RemoverDefinition rd) {
        MarcSubfield before = null;
        MarcSubfield now = null;
        for (Object o : fld.getSubfields()) {
            before = now;
            now = (MarcSubfield)o;
            String regExp = (String)rd.in.get(String.valueOf(now.getCode()));
            if (regExp != null) {
                removePontuation(now, regExp);
            }
            regExp = (String)rd.before.get(String.valueOf(now.getCode()));
            if (before != null && regExp != null) {
                removePontuation(before, regExp);
            }
        }
    }

    /**
     * @param sf
     * @param regExp
     */
    protected static void removePontuation(MarcSubfield sf, String regExp) {
        String value = sf.getValue();
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        String newValue = m.replaceAll("");
        sf.setValue(newValue);
    }

    /**
     */
    protected static class RemoverDefinition {
        public String  field;
        public HashMap before;
        public HashMap in;

        /**
         * Creates a new instance of this class.
         * 
         * @param field
         */
        public RemoverDefinition(String field) {
            this.field = field;
            before = new HashMap();
            in = new HashMap();
        }

        /**
         * @param subfield
         * @param regExp
         */
        public void addIn(String subfield, String regExp) {
            in.put(subfield, regExp);
        }

        /**
         * @param subfield
         * @param regExp
         */
        public void addBefore(String subfield, String regExp) {
            before.put(subfield, regExp);
        }
    }
}
