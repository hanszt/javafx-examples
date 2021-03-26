package com.gluonhq.samples.cloudfirst.service;

import com.gluonhq.cloudlink.client.data.DataClient;
import com.gluonhq.cloudlink.client.data.DataClientBuilder;
import com.gluonhq.cloudlink.client.data.OperationMode;
import com.gluonhq.cloudlink.client.data.SyncFlag;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.samples.cloudfirst.model.Note;
import javax.annotation.PostConstruct;

public class Service {
    
    private static final String NOTES = "notes-cloudfirst";

    private GluonObservableList<Note> noteGluonObservableList;
    
    private DataClient dataClient;
    
    @PostConstruct
    public void postConstruct() {
        dataClient = DataClientBuilder.create()
                .operationMode(OperationMode.CLOUD_FIRST)
                .build();
        
        noteGluonObservableList = retrieveNotes();
    }
    
    private GluonObservableList<Note> retrieveNotes() {
        // Retrieve notes from cloud or local storage
        return DataProvider.retrieveList(
                dataClient.createListDataReader(NOTES, Note.class,
                SyncFlag.LIST_WRITE_THROUGH,
                SyncFlag.OBJECT_WRITE_THROUGH));
    }
    
    public void addNote(Note note) {
        noteGluonObservableList.add(note);
    }

    public void removeNote(Note note) {
        noteGluonObservableList.remove(note);
    }

    public GluonObservableList<Note> getNoteGluonObservableList() {
        return noteGluonObservableList;
    }
}
