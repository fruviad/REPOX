/*
 * Created on 15/Mar/2006
 *
 */
package pt.utl.ist.repox.accessPoint;

import pt.utl.ist.repox.recordPackage.RecordRepox;
import pt.utl.ist.repox.util.IndexUtil;

import java.util.Collection;
import java.util.List;

/**
 * Contains a definition and an implementation of a particular access point in a
 * DataSource This class is responsible for extracting the values to be indexed
 * from a record
 * 
 * @author Nuno Freire
 * 
 */
public abstract class AccessPoint {
    /** AccessPoint PREFIX_INTERNAL_BD */
    public static final String PREFIX_INTERNAL_BD          = "repox_";
    /** AccessPoint SUFIX_TIMESTAMP_INTERNAL_BD */
    public static final String SUFIX_TIMESTAMP_INTERNAL_BD = "_timestamp";
    /** AccessPoint SUFIX_RECORD_INTERNAL_BD */
    public static final String SUFIX_RECORD_INTERNAL_BD    = "_record";

    //Id of the AccessPoint - unique within the DataSource
    protected String           id;
    // Should the records marked as deleted be indexed by this AcessPoint?
    protected boolean          indexDeletedRecords         = false;
    //How should the records be prepared for indexing
    protected boolean          tokenizable                 = true;
    protected boolean          removeAllSpaces             = false;
    protected boolean          indexEncoded                = true;

    //is this an access point defined internally by REPOX?
    protected boolean          repoxInternal               = false;

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     */
    public AccessPoint(String id) {
        this.id = id;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     * @param tokenizable
     */
    public AccessPoint(String id, boolean tokenizable) {
        this(id);
        this.tokenizable = tokenizable;
    }

    /**
     * Extracts the values to index from a RecordNode
     * 
     * @param record
     * @return Collection
     */
    public abstract Collection index(RecordRepox record);

    /**
     * @param records
     * @return List
     */
    public abstract List index(List<RecordRepox> records);

    @SuppressWarnings("javadoc")
    public String getId() {
        return id;
    }

    @SuppressWarnings("javadoc")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the type of values in the index (String, Integer, etc)
     */
    public abstract Class typeOfIndex();

    @SuppressWarnings("javadoc")
    public boolean isTokenizable() {
        return tokenizable;
    }

    @SuppressWarnings("javadoc")
    public void setTokenizable(boolean tokenizable) {
        this.tokenizable = tokenizable;
    }

    /**
     * prepares a value for indexing
     * 
     * @param value
     * @return String
     */
    public String indexValue(String value) {
        if (indexEncoded) {
            return IndexUtil.encode(value, removeAllSpaces ? IndexUtil.RemoveAllSpaces.REMOVE : IndexUtil.RemoveAllSpaces.DONT_REMOVE);
        } else {
            return value;
        }
    }

    //public abstract Collection<String> indexDomain(RecordNode record);
    @SuppressWarnings("javadoc")
    public boolean isIndexDeletedRecords() {
        return indexDeletedRecords;
    }

    @SuppressWarnings("javadoc")
    public void setIndexDeletedRecords(boolean indexDeletedRecords) {
        this.indexDeletedRecords = indexDeletedRecords;
    }

    @SuppressWarnings("javadoc")
    public boolean isRemoveAllSpaces() {
        return removeAllSpaces;
    }

    @SuppressWarnings("javadoc")
    public void setRemoveAllSpaces(boolean removeAllSpaces) {
        this.removeAllSpaces = removeAllSpaces;
    }

    @SuppressWarnings("javadoc")
    public boolean isRepoxInternal() {
        return repoxInternal;
    }

    @SuppressWarnings("javadoc")
    public void setRepoxInternal(boolean repoxInternal) {
        this.repoxInternal = repoxInternal;
    }

    @SuppressWarnings("javadoc")
    public boolean isIndexEncoded() {
        return indexEncoded;
    }

    @SuppressWarnings("javadoc")
    public void setIndexEncoded(boolean indexEncoded) {
        this.indexEncoded = indexEncoded;
    }

}
