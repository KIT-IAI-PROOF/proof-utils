/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

import java.util.Objects;

import edu.kit.iai.webis.proofutils.message.BaseMessage;
import edu.kit.iai.webis.proofutils.message.Error;
import edu.kit.iai.webis.proofutils.message.IMessage;
import edu.kit.iai.webis.proofutils.message.MessageType;
import edu.kit.iai.webis.proofutils.message.NotifyMessage;
import edu.kit.iai.webis.proofutils.message.SyncMessage;
import edu.kit.iai.webis.proofutils.message.ValueMessage;
import edu.kit.iai.webis.proofutils.model.SimulationStatus;
import edu.kit.iai.webis.proofutils.model.SimulationPhase;

/**
 * Builder pattern to create messages in the proof environment
 */
public class MessageBuilder {

	/**
	 * initialize the building process for a message <br>
	 * <b>Note:</b> If a time stamp is not explicitly set, it will be created automatically on the basis of:<br>
	 * 'LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)'
	 *
	 * @param messageType
	 *            the {@link MessageType} as a mandatory attribute
	 * @return a builder helper class to collect the attribute values
	 */
	public static MsgBuilder init( MessageType messageType ) {
		return new MsgBuilder(messageType);
	}

	/**
	 * builder helper class to collect the attribute values
	 */
	public static class MsgBuilder {
		private Long dateTime;
		private MessageType messageType;
		private Object data;
		private SimulationPhase simulationPhase;
		private SimulationStatus blockStatus;
		private String workflowId;
		private String globalBlockId;
		private Integer localBlockId;
		private String executionId;
		private Integer communicationPoint;
		private Integer communicationStepSize;
		private IMessage messageToCopy;
		private String errorText;
		private Error thrownError;

		private MsgBuilder() {
		}

		/**
		 * create and initialize an instance of the builder helper class
		 *
		 * @param messageType
		 *            the (mandatory) message type
		 */
		private MsgBuilder( MessageType messageType ) {
			this.messageType = messageType;
		}

		/**
		 * build the final instance of the desired message
		 *
		 * @return the instance of the desired message
		 */
		public IMessage build() {

			BaseMessage baseMsg;

			switch( this.messageType ) {
			case VALUE -> {
				baseMsg = new ValueMessage();
				if( this.messageToCopy != null ) {
					this.copyMessageAttributesTo(baseMsg);
				}
				((ValueMessage) baseMsg).setData(this.data);
			}
			case SYNC -> {
				baseMsg = new SyncMessage();
				if( this.messageToCopy != null ) {
					this.copyMessageAttributesTo(baseMsg);
				}
				((SyncMessage) baseMsg).setCommunicationStepSize(this.communicationStepSize);
			}
			case NOTIFY -> {
				baseMsg = new NotifyMessage();
				if( this.messageToCopy != null ) {
					this.copyMessageAttributesTo(baseMsg);
				}
				Objects.requireNonNull(this.blockStatus, "BlockStatus must be given for NotifyMessage!");
				((NotifyMessage) baseMsg).setBlockStatus(this.blockStatus);
				((NotifyMessage) baseMsg).setThrownError(this.thrownError);
				((NotifyMessage) baseMsg).setErrorText(this.errorText);
			}
			default -> throw new IllegalArgumentException("Message type %s not supported".formatted(this.messageType));
			}

			// override settings if there are some
			if( this.communicationPoint != null ) {
				baseMsg.setCommunicationPoint(this.communicationPoint);
			}
			if( this.workflowId != null ) {
				baseMsg.setWorkflowId(this.workflowId);
			}
			if( this.executionId != null ) {
				baseMsg.setExecutionId(this.executionId);
			}
			if( this.globalBlockId != null ) {
				baseMsg.setGlobalBlockId(this.globalBlockId);
			}
			if( this.localBlockId != null ) {
				baseMsg.setLocalBlockId(this.localBlockId);
			}
			if( this.simulationPhase != null ) {
				baseMsg.setSimulationPhase(this.simulationPhase);
			}

			// always set a new time stamp
			long timeInMillis = System.currentTimeMillis();
			baseMsg.setTime(this.dateTime != null ? (long)(this.dateTime / 1000) : (long)(timeInMillis / 1000) );
			baseMsg.setTimeInMillis(this.dateTime != null ? this.dateTime : timeInMillis );

			return baseMsg;
		}

		private void copyMessageAttributesTo( BaseMessage baseMsg ) {
			baseMsg.setSimulationPhase( this.messageToCopy.getSimulationPhase() );
	    	baseMsg.setLocalBlockId( this.messageToCopy.getLocalBlockId() );
	    	baseMsg.setGlobalBlockId( this.messageToCopy.getGlobalBlockId() );
	    	baseMsg.setExecutionId( this.messageToCopy.getExecutionId() );
	    	baseMsg.setWorkflowId( this.messageToCopy.getWorkflowId() );
		}

		/**
		 * set the date and time as a time stamp
		 *
		 * @param dateTime
		 *            the date and time in UNIX long format (milliseconds)
		 * @return the current builder instance
		 */
		public MsgBuilder dateTime( Long dateTime ) {
			this.dateTime = dateTime;
			return this;
		}

		/**
		 * copy the contents of a message to a new one.
		 * Other settings can be made that overrides the settings of the copied message
		 * <br><b>Note: </b> if the message to copy is of an other {@link MessageType} than
		 * given by the {@link MessageBuilder#init} method, only the base attributes are copied
		 * (see {@link BaseMessage})
		 * @param message the message to be copied from
		 * @return the current builder instance
		 */
		public MsgBuilder copyOf( IMessage message ) {
			this.messageToCopy = message;
			return this;
		}

		/**
		 * add data (payload) to the message.
		 * <br><b>Note: </b> the data attachment is only valid for {@link ValueMessage}s.
		 * If other message types are used, the attachment is ignored.
		 * @param data the data
		 * @return the current builder instance
		 */
		public MsgBuilder data( Object data ) {
			this.data = data;
			return this;
		}

		/**
		 * set the current workflow phase
		 * @param wfPhase the workflow phase
		 * @return the current builder instance
		 */
		public MsgBuilder simulationPhase( SimulationPhase wfPhase ) {
			this.simulationPhase = wfPhase;
			return this;
		}

		/**
		 * set the {@link SimulationStatus} for this message
		 * <br><b>Note: </b> the block status setting is only valid for {@link NotifyMessage}s.
		 * If other message types are used, the block status is ignored.
		 * @param blockStatus  the block status
		 * @return the current builder instance
		 */
		public MsgBuilder blockStatus( SimulationStatus blockStatus ) {
			this.blockStatus = blockStatus;
			return this;
		}

		/**
		 * set the current workflow id for this message
		 * @param workflowId the current workflow id
		 * @return the current builder instance
		 */
		public MsgBuilder workflowId( String workflowId ) {
			this.workflowId = workflowId;
			return this;
		}

		/**
		 * set the global id (UUID) of the originating block for this message
		 * @param globalBlockId the global id (UUID)
		 * @return the current builder instance
		 */
		public MsgBuilder globalBlockId( String globalBlockId ) {
			this.globalBlockId = globalBlockId;
			return this;
		}

		/**
		 * set the current execution id for this message
		 * @param executionId the current execution id
		 * @return the current builder instance
		 */
		public MsgBuilder executionId( String executionId ) {
			this.executionId = executionId;
			return this;
		}

		/**
		 * set the local id (sequential number) of the originating block for this message
		 * @param localBlockId the local id (sequential number)
		 * @return the current builder instance
		 */
		public MsgBuilder localBlockId( Integer localBlockId ) {
			this.localBlockId = localBlockId;
			return this;
		}

	    /**
	     * set the communication point (step position/number) for this message
		 * <br><b>Note: </b> the communication point setting is only valid for {@link SyncMessage}s.
		 * If other message types are used, the communication point is ignored.
	     * @param communicationPoint the communication point
		 * @return the current builder instance
	     */
		public MsgBuilder communicationPoint( Integer communicationPoint ) {
			this.communicationPoint = communicationPoint;
			return this;
		}

		/**
		 * set the communication step size (temporal step distance) for this message
		 * <br><b>Note: </b> the communication step size setting is only valid for {@link SyncMessage}s.
		 * If other message types are used, the communication step size is ignored.
		 * @param communicationStepSize the communication step size
		 * @return the current builder instance
		 */
		public MsgBuilder communicationStepSize( Integer communicationStepSize ) {
			this.communicationStepSize = communicationStepSize;
			return this;
		}

		/**
		 * set the error text (if given) for a {@link NotifyMessage}
		 * @param errorText the error text
		 * @return the current builder instance
		 */
		public MsgBuilder errorText( String errorText ) {
			this.errorText = errorText;
			return this;
		}

		/**
		 * set the Throwable ({@link Exception} or {@link Error}) for a  {@link NotifyMessage}
		 * @param thrownError the Error or Exception thrown
		 * @return the current builder instance
		 */
		public MsgBuilder thrownError( Error thrownError ) {
			this.thrownError = thrownError;
			return this;
		}

	}
}
