/*
 * Copyright (c) 2012 by the original author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.powertac.logtool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.powertac.logtool.common.DomainObjectReader;
import org.powertac.logtool.common.MissingDomainObject;
import org.powertac.logtool.common.DomainBuilder;
import org.powertac.logtool.ifc.Analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Reads a state log file, re-creates and updates objects, calls
 * listeners.
 * @author John Collins
 */
@Service
public class LogtoolCore
{
  static private Logger log = Logger.getLogger(LogtoolCore.class.getName());

  @Autowired
  private DomainObjectReader reader;
  
  @Autowired
  private DomainBuilder builder;

  /**
   * Default constructor
   */
  public LogtoolCore ()
  {
    super();
  }
  
  /**
   * Processes a command line. For now, it's just the name of a
   * state-log file from the simulation server.
   */
  public void processCmdLine (String[] args)
  {
    if (args.length < 2) {
      System.out.println("Usage: Logtool file analyzer ...");
      return;
    }
    String filename = args[0];

    ArrayList<Analyzer> tools = new ArrayList<Analyzer>();
    for (int i = 1; i < args.length; i++) {
      try {
        Class<?> toolClass = Class.forName(args[i]);
        tools.add((Analyzer)toolClass.newInstance());
      }
      catch (ClassNotFoundException e1) {
        System.out.println("Cannot find analyzer class " + args[i]);
      }
      catch (Exception ex) {
        System.out.println("Exception creating analyzer " + ex.toString());
      }
    }

    readStateLog(filename, tools);
  }

  /**
   * Reads the given state-log file using the DomainObjectReader
   */
  public void readStateLog (String filename, List<Analyzer> tools)
  {
    File input = new File(filename);
    String line = null;
    if (!input.canRead()) {
      System.out.println("Cannot read file " + filename);
      return;
    }
    log.info("Reading file " + filename);    
    try {
      builder.setup();
      for (Analyzer tool : tools) {
        tool.setup();
      }
      BufferedReader in =
              new BufferedReader(new FileReader(input));
      while (true) {
        line = in.readLine();
        if (null == line)
          break;
        reader.readObject(line);
      }
      builder.report();
      for (Analyzer tool : tools) {
        tool.report();
      }
    }
    catch (FileNotFoundException e) {
      System.out.println("Cannot open file " + filename);
    }
    catch (IOException e) {
      System.out.println("error reading from file " + filename);
    }
    catch (MissingDomainObject e) {
      System.out.println("MDO on " + line);
    }
  }
  
  /**
   * Reads the given state log with a single analyzer
   */
  public void readStateLog(String filename, Analyzer tool)
  {
    ArrayList<Analyzer> tools = new ArrayList<Analyzer>();
    tools.add(tool);
    readStateLog(filename, tools);
  }
}
