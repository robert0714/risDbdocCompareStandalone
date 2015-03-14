package org.robert.study.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.iisi.rl.table.script.ScriptTableInfo;

public interface ScanInspectService {

    public void createSQL(final ScriptTableInfo table, final File destiFile) throws IOException;

    public ScriptTableInfo extractTable(final List<String> srcList);

    public List<ScriptTableInfo> covertFromFolder(final File direction);

    public ScriptTableInfo convertTOUnit(final File aFile) throws RuntimeException;

    public List<File> getQualifiedFile(final File folder) throws IOException;

}
