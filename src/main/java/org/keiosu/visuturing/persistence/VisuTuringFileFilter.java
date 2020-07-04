package org.keiosu.visuturing.persistence;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileFilter;

public class VisuTuringFileFilter extends FileFilter implements FilenameFilter {
  private List<String> validExtensions = new ArrayList<>();
  private String description = "VisuTuring Files";

  public VisuTuringFileFilter() {
    // do nothing
  }

  public void addExtension(String extension) {
    this.validExtensions.add(extension.toLowerCase());
  }

  public boolean accept(File var1) {
    if (var1.isDirectory()) {
      return true;
    } else {
      String var2 = var1.getName().toLowerCase();

      for(int var3 = this.validExtensions.size() - 1; var3 >= 0; --var3) {
        if (var2.endsWith(this.validExtensions.get(var3))) {
          return true;
        }
      }

      return false;
    }
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String var1) {
    this.description = var1;
  }

  public boolean accept(File var1, String var2) {
    for(int var3 = this.validExtensions.size() - 1; var3 >= 0; --var3) {
      if (var2.endsWith(this.validExtensions.get(var3))) {
        return true;
      }
    }

    return false;
  }
}