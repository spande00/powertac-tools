/*
 * Copyright 2011, 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.powertac.logtool;

//import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is the top level of the Power TAC server.
 * @author John Collins
 */
public class Logtool
{
  /**
   * Sets up the logtool, delegates everything to a LogtoolCore instance.
   */
  public static void main (String[] args)
  {
    LogtoolCore runner = new LogtoolCore();
    runner.processCmdLine(args);
    
    // if we get here, it's time to exit
    System.exit(0);
  }
}
