/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import edu.kit.iai.webis.proofutils.message.IMessage;
import edu.kit.iai.webis.proofutils.message.NotifyMessage;
import edu.kit.iai.webis.proofutils.message.SyncMessage;
import edu.kit.iai.webis.proofutils.message.ValueMessage;
import edu.kit.iai.webis.proofutils.model.SimulationPhase;

import org.slf4j.spi.LocationAwareLogger;

/**
 * Helper class for logging messages.
 * It is realized as a builder pattern to only have to provide the necessary attributes.
 * Furthermore, a colored output is provided for level-based message highlight.
 * The LoggingHelpper uses the slf4j2 package that is realized by the logback package. This means, that the configuration can be made by a logback.xml file.
 */
@Component
public class LoggingHelper{

    private static final Logger consoleLogger = LoggerFactory.getLogger("LoggingHelper");

    static {
    	((ch.qos.logback.classic.Logger)consoleLogger).setLevel(ch.qos.logback.classic.Level.INFO);
    }

    private static Level logLevel = Level.INFO;
    
//    private static final Logger eventLogger = LoggerFactory.getLogger("EventLogger");

    private static boolean logSourcePosition = false;
    private static boolean printColor = true;
    private static final String MDC_SIMPLIFIED_FORMAT = "simplifiedFormat";
    private final static char CRLF = '\n';
    private final static int DEFAULT_MSG_LENGTH = 512;  // used for the capacity of the used StringBuilder. If it is too short, SB extends its capacity automatically

    /**
     * Log an info message
     * @return a builder instance for further use
     */
    public static LogBuilder info() {
    	return new LogBuilder( Level.INFO );
    }

    /**
     * Log a warning message
     * @return a builder instance for further use
     */
    public static LogBuilder warn() {
    	return new LogBuilder( Level.WARN );
    }

    /**
     * Log an error message
     * @return a builder instance for further use
     */
    public static LogBuilder error() {
    	return new LogBuilder( Level.ERROR );
    }

    /**
     * Log a debug message
     * @return a builder instance for further use
     */
    public static LogBuilder debug() {
    	return new LogBuilder( Level.DEBUG );
    }

    /**
     * Log a trace message
     * @return a builder instance for further use
     */
    public static LogBuilder trace() {
    	return new LogBuilder( Level.TRACE );
    }

    /**
     * decide whether all messages should be printed with color or not. This can be
     * overridden for single log messages with builder method {@link LogBuilder#printColored()}.
     * @param printColored if true, use colors, if false, do not use colors
     */
    public static void printColored( boolean printColored ) {
    	printColor = printColored;
    }

    /**
     * switch on or off the output of a StackTrace. For simple logging, the class name and
     * method name can be printed.
     * @param log if true, the StackTrace element is printed, if false, it is not printed
     */
    public static void logSourcePosition( boolean log ) {
    	logSourcePosition = log;
    }

    /**
     * Set the MDC key "simplifiedFormat" to enable simple message format logging.
     * When set to "true", log messages will be output with only the message text
     * (no timestamp, log level, or logger name) based on the logback.xml configuration.
     * <br><br>
     * This requires the logback.xml to have appenders configured with MDCValueFilter
     * for the "simplifiedFormat" key (see proof-worker/src/main/resources/logback.xml).
     * <br><br>
     * Usage example:
     * <pre>{@code
     * // Enable simple format temporarily
     * LoggingHelper.setSimplifiedFormat(true);
     * LoggingHelper.info().log("Just the message without full SLF4J format");
     * LoggingHelper.setSimplifiedFormat(false);
     * }</pre>
     * @param useSimplified if "true", use simple message format; if "false" or null, use full format
     */
    public static void setSimplifiedFormat( boolean useSimplified ) {
    	if (useSimplified) {
    		MDC.put(MDC_SIMPLIFIED_FORMAT, "true");
    	} else {
    		MDC.remove(MDC_SIMPLIFIED_FORMAT);
    	}
    }

    /**
     * Set the logging level as a String for the next messages. <br><br>
     * <b>Note: this method is not available with basic SLF4J API. It only works with the logback-classic package</b><br>
     * @param level the new logging level as a String. See: {@link org.slf4j.event.Level}
     */
    public static void setLogLevel( String level ) {
    	// both loggers have the same class definition
    	try {
    		setLoggingLevel(Level.valueOf(level));
		} catch (Exception e) {
			consoleLogger.error( "Could not set new Logging Level " + level + "!" );
		}
    }

    /**
     * Set the logging level for the next messages. <br><br>
     * <b>Note: this method is not available with basic SLF4J API. It only works with the logback-classic package</b><br>
     * @param level the {@link org.slf4j.event.Level}
     */
    public static void setLogLevel( Level level ) {
    	setLoggingLevel(level);
    }

    private static void setLoggingLevel( Level level ) {
    	// both loggers have the same class definition
    	if( consoleLogger instanceof ch.qos.logback.classic.Logger qosLogger) {
    		ch.qos.logback.classic.Level qosLevel = ch.qos.logback.classic.Level.convertAnSLF4JLevel(level);
    		((ch.qos.logback.classic.Logger)consoleLogger).setLevel(qosLevel);
    		logLevel = level;
    		consoleLogger.info( "Level set by program from " + qosLogger.getLevel() + " to " + level + " eventually overriding logback settings ... ");
    		return;
    	}
    	consoleLogger.warn( "Could not set new Logging Level! (Logger is instance of " + consoleLogger.getClass().getName() );
    }

    /**
     * Get the current logging level number (ordinal). <br><br>
     * <b>Note: this method is not available with basic SLF4J API. It only works with the logback-classic package</b><br>
     *
     * @return the number of the current {@link ch.qos.logback.classic.Level}, <b>converted to the numbers defined in SLF4J</b>.
     */
    public static int getLogLevelInt() {
    	// both loggers have the same class definition
    	if( consoleLogger instanceof ch.qos.logback.classic.Logger qosLogger) {
    		ch.qos.logback.classic.Level lvl = qosLogger.getLevel();
    		if( lvl != null ) {
    			int lvlInt = lvl.toInt();
    			// the level numbers are for  TRACE, DEBUG, INFO, WARN, and ERROR:
    			// for org.slf4j.event.Level:   0,    10,    20,   30,        40
    			// for ch.qos.logback.Level:  5000,  10000, 20000, 30000,   40000
    			return lvlInt < 50000 ? lvlInt / 1000 : 0;
    		}
    		else {
    			consoleLogger.warn( "Given Logger has no LOG Level set!" );
    		}
    	}
		consoleLogger.warn( "Could not determine correct Level number! (Logger is instance of "
							+ consoleLogger.getClass().getName()
							+ ") => returning 20 (int value for 'INFO') ... ");
    	return org.slf4j.event.Level.INFO.toInt();
    }

    /**
     * Get the current logging level. <br><br>
     * <b>Note: this method is not available with basic SLF4J API. It only works with the logback-classic package</b><br>
     *
     * @return the name (String) of the current {@link ch.qos.logback.classic.Level}
     */
    public static String getLogLevel() {
    	return logLevel.name();
    }

    /**
     * Get the current logging level and return a corresponding {@link org.slf4j.event.Level}. <br><br>
     * <b>Note: this method is not available with basic SLF4J API. It only works with the logback-classic package</b><br>
     *
     * @return the corresponding {@link org.slf4j.event.Level} for the used {@link ch.qos.logback.classic.Level} or Level.INFO for the other values of {@link ch.qos.logback.classic.Level}
     */
    public static Level getSLF4JLogLevel() {
    	// both loggers have the same class definition
    	if( consoleLogger instanceof ch.qos.logback.classic.Logger qosLogger) {
    		ch.qos.logback.classic.Level lvl = qosLogger.getLevel();
    		switch (lvl.levelInt) {
			case 5000: {
				return Level.TRACE;
			}
			case 10000: {
				return Level.DEBUG;
			}
			case 20000: {
				return Level.INFO;
			}
			case 30000: {
				return Level.WARN;
			}
			case 40000: {
				return Level.ERROR;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + lvl.levelInt);
			}
    	}
    	return Level.INFO;
    }

    /**
     * Check whether the current logging level is either TRACE or DEBUG for a detailed output.<br><br>
     * <b>Note: this method is not available with basic SLF4J API. It only works with the logback-classic package</b><br>
     *
     * @return true, if the current logging level is either TRACE or DEBUG
     */
    public static boolean isLevelDebugOrTrace() {
    	return getLogLevelInt() <= 10;
    }

    /**
     * the builder class
     */
    public final static class LogBuilder {
    	private Level currentLevel = null;
    	private String messageText = null;
    	private String workflowId = null;
    	private String executionId = null;
    	private Integer localBlockId = -1;
    	private String globalBlockId = null;
    	private SimulationPhase phase;
    	private Object data = null;
    	private Map<String, String> mdcMap = null;
    	private boolean printWithColor = printColor;
    	private boolean printStackTrace = false;
		private boolean printBorder = false;
    	private String ColBlue = "";
    	private String ColPurple = "";
    	private String ColRed = "";
		private String ColReset = "";
		private String ColMsg = ""; //Colors.ANSI_BLACK.value();
    	private Exception exception;
		private Exception logEx = null;
    	private IMessage messageObject;

		/**
    	 * set the log level (see org.slf4j.event.Level)
    	 * @param level the log level, may be either Level.TRACE, Level.DEBUG,
    	 * Level.INFO, Level.WARN, or Level.ERROR
    	 */
    	protected LogBuilder(Level level) {
    		this.currentLevel = level;
    	}

    	/**
    	 * set existing parameters provided by a message object ({@link SyncMessage}, {@link NotifyMessage}, or {@link ValueMessage})
    	 * <br><br>Used attributes from message:
    	 * <ul>
    	 * <li>localBlockId</li>
    	 * <li>globalBlockId</li>
    	 * <li>workflowId</li>
    	 * <li>phase</li>
    	 * <li>executionId</li>
    	 * </ul>
    	 * @param messageObject the message object, either {@link SyncMessage}, {@link NotifyMessage}, or {@link ValueMessage}.
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder messageObject(IMessage messageObject) {
    		this.messageObject = messageObject;
    		return this;
    	}

    	/**
    	 * log the workflow id with MDC
    	 * @param workflowId the workflow id
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder workflowId(String workflowId) {
    		this.workflowId = workflowId;
    		return this;
    	}

    	/**
    	 * log the execution id with MDC
    	 * @param executionId the execution id
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder executionId(String executionId) {
    		this.executionId = executionId;
    		return this;
    	}

    	/**
    	 * log the number of the block (localBlockId) with MDC
    	 * @param localBlockId the local block id inside  workspace
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder localBlockId(Integer localBlockId) {
    		this.localBlockId = localBlockId;
    		return this;
    	}

    	/**
    	 * log the global block Id (UUID) with MDC
    	 * @param globalBlockId the global block id
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder globalBlockId(String globalBlockId) {
    		this.globalBlockId = globalBlockId;
    		return this;
    	}

    	/**
    	 * log the current {@link SimulationPhase} with MDC
    	 * @param phase the phase
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder simulationPhase(SimulationPhase phase) {
    		this.phase = phase;
    		return this;
    	}

    	/**
    	 * log additional infos with MDC, whose output can be declared in file
    	 * 'src/main/resources/logback.xml'
    	 * @param mdcMap the map containing the additional infos as a key-value pair
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder additionalMDC(Map<String, String> mdcMap) {
    		this.mdcMap = mdcMap;
    		return this;
    	}

    	/**
    	 * log any data  with MDC
    	 * @param data	the data. It will be printed as a String
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder data(Object data) {
    		this.data = data;
    		return this;
    	}

    	/**
    	 * decide whether this message should be printed with colors. This option
    	 * overwrites the option for all messages (see {@link LoggingHelper#printColored(boolean)})
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder printColored() {
    		this.printWithColor = true;
    		return this;
    	}

    	/**
    	 * log a exception message
    	 * @param exception the exception whose message will be logged
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder exception(Exception exception) {
    		this.exception = exception;
    		return this;
    	}

    	/**
    	 * log a exception message and print the exception stack trace
    	 * @param exception the exception whose message and stack trace will be logged
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder printStackTrace(Exception exception) {
    		this.exception = exception;
    		this.printStackTrace = true;
    		return this;
    	}

    	/**
    	 * Print the stack trace of the thread (see {@link this#getStackTrace()})
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder printStackTrace() {
    		this.printStackTrace = true;
    		return this;
    	}

    	/**
    	 * print a message with a given color from the color table. <br>
    	 * Note: The attribute {@link #printColored} must not be set to false.
    	 * @param messageColor the desired {@link Colors}
    	 * @return the builder instance for further use
    	 */
    	public LogBuilder messageColor( Colors messageColor ) {
    		this.ColMsg = messageColor.value();
    		return this;
    	}

		/**
		 * if true, print a border around the message to highlight it. The border is printed only for this message. It is recommended to use this feature rarely and it is only useful when logging on console is used.
		 *
		 * @return the builder instance for further use
		 */
		public LogBuilder withBorder() {
			this.printBorder = true;
			return this;
		}

    	private final static String MDC_BLOCK_ID = "blockId";
    	private final static String MDC_GLOBAL_BLOCK_ID = "globalBlockId";
    	private final static String MDC_PHASE = "phase";
    	private final static String MDC_DATA = "data";
    	private final static String MDC_WORKFLOW_ID = "workflowId";
    	private final static String MDC_EXECUTION_ID = "executionId";

    	/**
    	 * check whether the message for the given level should be printed 
    	 */
		private boolean checkLevel() {
			return this.currentLevel.toInt() >= logLevel.toInt();
		}

    	/**
    	 * log (print) a message text with a given format string and values to build the final message
    	 *
    	 * @param format the format (see: {@link String#format(String, Object...) }
     	 * @param values a list of values
    	 */
    	public void log(String format, Object ... values ) {
    		if( this.checkLevel() ){
    			this.logMessage( LoggingHelper.consoleLogger, String.format(format, values) );
    		}
    	}

    	/**
    	 * pure logging of the message
    	 * @param logger (still present to ensure flexibility in future)
    	 * @param pureMessageText
    	 */
        private void logMessage(Logger logger, String pureMessageText) {
        	
        	this.messageText = ( pureMessageText == null || pureMessageText.isEmpty() ? "-- no message --" : pureMessageText);
			
			// reset colors if no colored output is desired
			if ( this.printWithColor) {
				this.ColBlue = Colors.ANSI_BLUE.value();
				this.ColPurple = Colors.ANSI_PURPLE.value();
		    	this.ColRed = Colors.ANSI_RED.value();
		    	this.ColReset = Colors.ANSI_RESET.value();
        	}

        	if( this.messageObject != null ) {
        		if( this.localBlockId == null ) {
					this.localBlockId = this.messageObject.getLocalBlockId();
				}
        		if( this.globalBlockId == null ) {
					this.globalBlockId = this.messageObject.getGlobalBlockId();
				}
        		if( this.workflowId == null ) {
					this.workflowId = this.messageObject.getWorkflowId();
				}
        		if( this.phase == null ) {
					this.phase = this.messageObject.getSimulationPhase();
				}
        		if( this.executionId == null ) {
					this.executionId = this.messageObject.getExecutionId();
				}
        	}

        	if( this.localBlockId != null ) {
        		MDC.put(MDC_BLOCK_ID, String.valueOf(this.localBlockId));
        	}
        	if( this.globalBlockId != null ) {
        		MDC.put(MDC_GLOBAL_BLOCK_ID, this.globalBlockId);
        	}
        	if( this.phase != null ) {
        		MDC.put(MDC_PHASE, this.phase.toString());
        	}
        	if( this.workflowId != null && ! this.workflowId.isEmpty() ) {
        		MDC.put(MDC_WORKFLOW_ID, String.valueOf(this.workflowId));
        	}
        	if( this.data != null ) {
        		MDC.put(MDC_DATA, this.data.toString());
        	}
        	if( this.executionId != null && ! this.executionId.isEmpty() ) {
        		MDC.put(MDC_EXECUTION_ID, String.valueOf(this.executionId));
        		this.messageText = this.ColBlue + this.executionId + this.ColReset + " " + this.messageText + this.ColReset;
        	}

        	if( this.mdcMap != null ) {
        		this.mdcMap.forEach( (k,v) -> {
        			MDC.put(k, v);
        		});
        	}

        	String resultingMessage = switch (this.currentLevel) {
	        	case INFO -> this.messageText;
				// Print stacktrace and exception message optionally. 
				case WARN, DEBUG, TRACE -> this.messageText + this.createExceptionLog(this.printStackTrace);
				// Print stacktrace always and exception message if available.
	        	case ERROR -> this.messageText + this.createExceptionLog(true);
	        	default -> "";
        	};
        	resultingMessage = this.enrichMessage(logger, this.currentLevel, resultingMessage);
        	Exception logEx = this.logEx;

            if ( logger instanceof LocationAwareLogger law ) {
                int levelInt = switch ( this.currentLevel ) {
                    case TRACE -> LocationAwareLogger.TRACE_INT;
                    case DEBUG -> LocationAwareLogger.DEBUG_INT;
                    case INFO  -> LocationAwareLogger.INFO_INT;
                    case WARN  -> LocationAwareLogger.WARN_INT;
                    case ERROR -> LocationAwareLogger.ERROR_INT;
                };
                // FQCN = die Klasse, in der der eigentliche Log-Aufruf steht (hier: die innere LogBuilder-Klasse).
                // Logback überspringt beim Stack-Walk alle Frames dieser Klasse und nimmt den Frame direkt darüber
                // als "echten" Aufrufer für %C / %M / %L.
                law.log( null, LoggingHelper.LogBuilder.class.getName(), levelInt, resultingMessage, null, logEx );
            } else {
                // Fallback, falls der Logger kein LocationAwareLogger ist (z.B. NOPLogger in Tests)
                switch ( this.currentLevel ) {
                    case TRACE -> logger.trace( resultingMessage, logEx );
                    case DEBUG -> logger.debug( resultingMessage, logEx );
                    case INFO  -> logger.info( resultingMessage, logEx );
                    case WARN  -> logger.warn( resultingMessage, logEx );
                    case ERROR -> logger.error( resultingMessage, logEx );
                }
            }
        	
        	
            MDC.clear();
        }

		private String enrichMessage(Logger logger, Level level, String message) {
			char[] border = new char[]{' '};
			int msgLen = message.length();
   			if( this.printBorder ) {
  	        	int borderLength = getMaxLineLength(message);
	        	border = new char[borderLength];
	        	Arrays.fill(border, '-');
	        	msgLen += (2 * borderLength);
   			}

   			StringBuilder sb = new StringBuilder(msgLen > DEFAULT_MSG_LENGTH ? msgLen : DEFAULT_MSG_LENGTH);

  			String levelColor = switch (level) {
  				case ERROR -> this.ColRed;
				case WARN -> this.ColPurple;
  				case INFO, DEBUG, TRACE -> this.ColMsg;
  				default -> this.ColMsg;
  			};
   			sb.append(levelColor );
   			
   			if( this.printBorder ) {
   				sb.append(CRLF).append(border).append(CRLF);
   			}
   			sb.append(message);
   			if( this.printBorder ) {
   				sb.append(CRLF).append(border).append(CRLF);
   			}
   			sb.append(this.ColReset);
   			return sb.toString();
        }

		
		private int getMaxLineLength(String message) {
			char[] msgArray = message.toCharArray();
			int maxLen = 0;
			int len = 0;
			for (int i = 0; i < msgArray.length; i++) {
				if( msgArray[i] == '\n'  &&  len > maxLen ) {
					maxLen = len;
					len = 0;
				}
				else if( msgArray[i] == '\t' ) {
					len += 8;
				}
				else {
					len++;
				}
			}
			return maxLen == 0 ? msgArray.length : maxLen;
		}

        /**
         * create the output for the exception message and its stack trace, if desired
         * @return the exception-dependent text to be logged
         */
        private String createExceptionLog(boolean addStackTrace) {
			String result = "";
			if( this.exception != null ) {
				// The stack trace itself is not embedded here: when requested, it is printed once by
				// the logger via the throwable argument passed to law.log(...) in logMessage().
				result = " -> " + this.exception.getMessage();
			}
			if (addStackTrace) {
				if (this.exception == null) {
					result += this.getStackTrace();
				}
				else {
					// When a stack trace is requested and exception available, pass the exception.
					// logback (and SLF4J's (msg, Throwable) overloads) auto-append the trace of any throwable received.
					this.logEx = this.exception;
				}
			}
			return result;
		}

		/**
         * get the calling class, method and line number for debugging
         * @return the relevant stack trace element that carries the calling method name
         */
        private String getStackTrace() {
        	if( logSourcePosition ) {
        		StackTraceElement[] steArray = new Throwable().getStackTrace();
        		int i = 3; // kann mit 3 beginnen, da StackTrace grundsaetzlich mit Thread vor LoggingHelper beginnt und damit kein ArrayOverFlow spaeter...
        		for( ; i < steArray.length ; ++i ){ // ++i: damit i nach verlassen der Schleife nicht veraendert wird
        			if( ! steArray[i].getClassName().startsWith( LoggingHelper.class.getName() )) {
						break;
					}
        		}
        		String sttr = "(" + steArray[i].getFileName() + ":" + steArray[ i ].getLineNumber() + ")::"
        				+ this.ColBlue + steArray[ i ].getMethodName() + ": " + this.ColReset;
        		return sttr;
        	}
        	return "";
        }

    } // class LogBuilder

    /**
     * print the Contents of a given immutable Map (Keys and Values) to a PrintStream (e.g. System.out)
     * for the debug level.
     *
     * @param map the Map (may be an immutable Map)
     * @param ps the PrintStream (e.g. System.out)
     * @param header an optional header that will be printed
     */
    public static <K,V> void printHashMapContents( Map<K, V> map, PrintStream ps, String header ) {
    	if( consoleLogger.isDebugEnabled() ) {
    		if( header != null ) {
				ps.append(header).append("\n");
			}
    		if( map != null ) {
    			if( map.isEmpty() ) {
					ps.append( "=> Map is empty!\n" );
				} else {
    				final int[] ii = new int[1];
    				map.keySet().forEach( e -> {
						String valueStr = "no-toString()-function-implemented";
						try {
							valueStr = map.get( e ).toString();
						}
						catch (Exception ex) {
							LoggingHelper.warn().log( "Could not determine value string representation for key '%s'! Exception: %s", e, ex.getMessage() );
						}
    					ps.append( "Key(" + ( ii[0]++ ) + "): " + e + " \tValue: " +  valueStr + "\n" );
    				});
    			}
    		}
    	}
    }

    /**
     * log the Contents of a given Map (Keys and Values) for the debug level.
     *
     * @param map the Map
     * @param header an optional header that will be printed
     */
    public static <K,V> void logHashMapContents( Map<K, V> map, String header ) {
    	if( consoleLogger.isDebugEnabled() ) {
    		if( header != null ) {
				consoleLogger.debug( header );
			}
    		if( map != null ) {
    			if( map.isEmpty() ) {
					consoleLogger.debug( "=> Map is empty!" );
				} else {
    				final int[] ii = new int[1];
    				map.keySet().forEach( e -> {
    					consoleLogger.debug( "Key(" + ( ii[0]++ ) + "): " + e + " \tValue: " + map.get( e ).toString() );
    				});
    			}
    		}
    	}
    }

    /**
     * log the Contents of a given Map (Keys and Values) for the debug level.
     *
     * @param map the Map
     * @param header an optional header that will be printed
     */
    public static <K> String getListContentsAsString( List<K> list ) {
		if( list != null ) {
			StringJoiner sj = new StringJoiner(",");
			list.forEach(k -> {
				sj.add(k.toString());
			});
			return(  list.size() > 0 ? sj.toString() : "empty list" );
		}
		return "no list given";
    }

    /**
     * log the Contents of a given Map (Keys and Values) for the debug level.
     *
     * @param map the Map
     * @param header an optional header that will be printed
     */
    public static <K> String getSetContentsAsString( Set<K> set ) {
    	if( set != null ) {
    		StringJoiner sj = new StringJoiner(",");
    		set.forEach(k -> {
    			sj.add(k.toString());
    		});
    		return(  set.size() > 0 ? sj.toString() : "empty set" );
    	}
    	return "no set given";
    }

    private final static char STAR = '*';
    private final static char BLNK = ' ';

    /**
     * print a text inside a border built of stars (asterisks)
     * @param text the text
     *
     * @return a String containing the bordered text
     */
    public static String printStarBordered( String text ) {
    	return printStarBordered( text, 2, 2 );
    }

    /**
     * print a text inside a border built of stars (asterisks)
     * @param text the text
     * @param numStars the number of stars building the left and right border of the rectangle
     * @param numBlanks the number of blanks between left/right border and the text
     * @return a String containing the bordered text
     */
    public static String printStarBordered( String text, int numStars, int numBlanks ) {
    	int len = text.length();
    	int lineLen = len+2*(numStars+numBlanks);

    	char[] lines = new char[3*lineLen+2];
    	Arrays.fill(lines, 0, lines.length, STAR);
    	lines[lineLen] = '\n';
    	lines[2*lineLen+1] = '\n';
    	char[] textc = text.toCharArray();
    	for (int i = lineLen+numStars+1, j=0; j < numBlanks ; i++,j++) {
    		lines[i] = BLNK;
    		lines[i+textc.length+numBlanks] = BLNK;
    	}
    	for (int i = lineLen+numStars+numBlanks+1, j = 0; j < textc.length; i++,j++) {
    		lines[i] = textc[j];
		}
    	return "\n" + new String(lines);
    }

}
