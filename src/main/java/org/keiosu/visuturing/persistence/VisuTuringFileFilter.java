package org.keiosu.visuturing.persistence;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.filechooser.FileFilter;

public class VisuTuringFileFilter extends FileFilter implements FilenameFilter {
  private List<String> validExtensions = new LinkedList<>();
  private String description = "VisuTuring Files";

  public VisuTuringFileFilter() {
    // do nothing
  }

  public void addExtension(String extension) {
    this.validExtensions.add(extension.toLowerCase());
  }

  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    } else {
      String filename = f.getName().toLowerCase();

      for (int idx = this.validExtensions.size() - 1; idx >= 0; --idx) {
        if (filename.endsWith(this.validExtensions.get(idx))) {
          return true;
        }
      }

      return false;
    }
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean accept(File dir, String name) {
    for (int i = validExtensions.size() - 1; i >= 0; --i) {
      if (name.endsWith(validExtensions.get(i))) {
        return true;
      }
    }

    return false;
  }
}
