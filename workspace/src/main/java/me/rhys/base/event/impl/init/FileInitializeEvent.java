package me.rhys.base.event.impl.init;

import lombok.AllArgsConstructor;
import me.rhys.base.event.Event;
import me.rhys.base.file.FileFactory;
import me.rhys.base.file.IFile;

import java.util.Arrays;

@AllArgsConstructor
public class FileInitializeEvent extends Event {

    private final FileFactory factory;

    public void register(IFile file) {
        factory.add(file);
    }

    public void register(IFile... files) {
        Arrays.stream(files).forEach(this::register);
    }

}
