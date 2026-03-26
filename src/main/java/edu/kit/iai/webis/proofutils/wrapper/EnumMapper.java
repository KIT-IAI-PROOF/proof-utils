/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import edu.kit.iai.webis.proofmodels.BlockDetail;
import edu.kit.iai.webis.proofmodels.ExecutionDetail;
import edu.kit.iai.webis.proofmodels.InputDetail;
import edu.kit.iai.webis.proofmodels.OutputDetail;
import edu.kit.iai.webis.proofmodels.ProgramDetail;
import edu.kit.iai.webis.proofmodels.TemplateDetail;
import edu.kit.iai.webis.proofmodels.WorkflowDetail;
import edu.kit.iai.webis.proofutils.model.SimulationStatus;
import edu.kit.iai.webis.proofutils.model.CommunicationType;
import edu.kit.iai.webis.proofutils.model.InterfaceType;
import edu.kit.iai.webis.proofutils.model.ProcessEnvironment;
import edu.kit.iai.webis.proofutils.model.SimulationPhase;
import edu.kit.iai.webis.proofutils.model.SimulationStrategy;
import edu.kit.iai.webis.proofutils.model.SyncStrategy;
import edu.kit.iai.webis.proofutils.wrapper.IOElement.DataType;
/**
 * A wrapper class to map Enums coming from database classes to Enums used by the wrapper classes and vice versa
 */
public class EnumMapper {

	/**
	 * get the {@link SimulationStatus} for a status defined in ExecutionDetail
	 * @param se the status defined in ExecutionDetail
	 * @return the corresponding {@link SimulationStatus}
	 */
    public static SimulationStatus getSimulationStatusFor( ExecutionDetail.StatusEnum se ) {
    	return SimulationStatus.values()[se.ordinal()];
    }

    public static ExecutionDetail.StatusEnum getExecutionStatusEnumFor( SimulationStatus bs ) {
    	return ExecutionDetail.StatusEnum.values()[bs.ordinal()];
    }

	/**
	 * get the {@link SimulationStatus} for a status defined in BlockDetail
	 * @param se the status defined in BlockDetail
	 * @return the corresponding {@link SimulationStatus}
	 */
    public static SimulationStatus getSimulationStatusFor( BlockDetail.StatusEnum se ) {
    	return SimulationStatus.values()[se.ordinal()];
    }

    /**
     * get the {@link SimulationStatus} for a status defined in TemplateDetail
     * @param se the status defined in TemplateDetail
     * @return the corresponding {@link SimulationStatus}
     */
    public static SimulationStatus getSimulationStatusFor( TemplateDetail.StatusEnum se ) {
    	return SimulationStatus.values()[se.ordinal()];
    }

    public static BlockDetail.StatusEnum getStatusEnumFor( SimulationStatus bs ) {
    	return BlockDetail.StatusEnum.values()[bs.ordinal()];
    }

    public static TemplateDetail.StatusEnum getSimulationStatusEnumFor( SimulationStatus bs ) {
    	return TemplateDetail.StatusEnum.values()[bs.ordinal()];
    }

	/**
	 * get the {@link SimulationPhase} for a simulation phase defined in InputDetail
	 * @param pe the simulation phase defined in InputDetail
	 * @return the corresponding {@link SimulationPhase}
	 */
    public static SimulationPhase getSimulationPhaseFor( InputDetail.PhaseEnum pe ) {
    	return SimulationPhase.values()[pe.ordinal()];
    }

    /**
     * get the {@link SimulationPhase} for a simulation phase defined in OutputDetail
     * @param pe the simulation phase defined in OutputDetail
     * @return the corresponding {@link SimulationPhase}
     */
    public static SimulationPhase getSimulationPhaseFor( OutputDetail.PhaseEnum pe ) {
    	return SimulationPhase.values()[pe.ordinal()];
    }

    /**
     * get the {@link InterfaceType} for a interface type defined in BlockDetail
     * @param te the interface type defined in BlockDetail
     * @return the corresponding {@link InterfaceType}. Default is {@link InterfaceType#STDIO}
     */
    public static InterfaceType getInterfaceTypeFor( BlockDetail.InterfaceTypeEnum te ) {
    	return te != null ? InterfaceType.values()[te.ordinal()] : InterfaceType.STDIO;
    }

    /**
     * get the {@link InterfaceType} for a interface type defined in ExecutionDetail
     * @param te the interface type defined in ExecutionDetail
     * @return the corresponding {@link InterfaceType}. Default is {@link InterfaceType#STDIO}
     */
    public static InterfaceType getInterfaceTypeFor( ExecutionDetail.InterfaceTypeEnum te ) {
    	return te != null ? InterfaceType.values()[te.ordinal()] : InterfaceType.STDIO;
    }

    public static InterfaceType getInterfaceTypeFor( TemplateDetail.InterfaceTypeEnum te ) {
    	return te != null ? InterfaceType.values()[te.ordinal()] : InterfaceType.STDIO;
    }

    public static BlockDetail.InterfaceTypeEnum getTypeEnumFor( InterfaceType it ) {
    	return BlockDetail.InterfaceTypeEnum.values()[it.ordinal()];
    }

    public static TemplateDetail.InterfaceTypeEnum getInterfaceTypeEnumFor( InterfaceType it ) {
    	return TemplateDetail.InterfaceTypeEnum.values()[it.ordinal()];
    }

    /**
     * get the {@link SimulationStrategy} for a simulation strategy defined in WorkflowDetail
     * @param sse the simulation strategy defined in WorkflowDetail
     * @return the corresponding {@link SimulationStrategy}. Default is {@link SimulationStrategy#WAIT_AND_CONTINUE}
     */
    public static SimulationStrategy getSimulationStrategyFor( WorkflowDetail.SimulationStrategyEnum sse ) {
    	return sse != null ? SimulationStrategy.values()[sse.ordinal()] : SimulationStrategy.WAIT_AND_CONTINUE;
    }

    /**
     * get the {@link SyncStrategy} for a sync strategy defined in BlockDetail
     * @param sse the sync strategy defined in BlockDetail
     * @return the corresponding {@link SyncStrategy}. Default is {@link SyncStrategy#WAIT_FOR_SYNC}
     */
    public static SyncStrategy getSyncStrategyFor( BlockDetail.SyncStrategyEnum sse ) {
    	return sse != null ? SyncStrategy.values()[sse.ordinal()] : SyncStrategy.WAIT_FOR_SYNC;
    }

    public static SyncStrategy getSyncStrategyFor( TemplateDetail.SyncStrategyEnum sse ) {
    	return sse != null ? SyncStrategy.values()[sse.ordinal()] : SyncStrategy.WAIT_FOR_SYNC;
    }

    public static WorkflowDetail.SimulationStrategyEnum getSimulationStrategyEnumFor( SimulationStrategy s ) {
    	return WorkflowDetail.SimulationStrategyEnum.values()[s.ordinal()];
    }

    /**
     * get the {@link IOElement.DataType} for a data type defined in InputDetail
     * @param te the data type defined in InputDetail
     * @return the corresponding {@link IOElement.DataType}. Default is {@link DataType#OBJECT}
     */
    public static IOElement.DataType getDataTypeFor( InputDetail.TypeEnum te ) {
    	return te != null ? DataType.values()[te.ordinal()] : DataType.OBJECT;
    }

    /**
     * get the {@link IOElement.DataType} for a data type defined in OutputDetail
     * @param te the data type defined in OutputDetail
     * @return the corresponding {@link IOElement.DataType}. Default is {@link DataType#OBJECT}
     */
    public static IOElement.DataType getDataTypeFor( OutputDetail.TypeEnum te ) {
    	return te != null ? DataType.values()[te.ordinal()] : DataType.OBJECT;
    }

    /**
     * get the {@link CommunicationType} for a communication type defined in InputDetail
     * @param ce the communication type defined in InputDetail
     * @return the corresponding {@link CommunicationType}
     */
    public static CommunicationType getCommunicationTypeFor( InputDetail.CommunicationTypeEnum ce ) {
    	return CommunicationType.values()[ce.ordinal()];
    }

    /**
     * get the {@link CommunicationType} for a communication type defined in OutputDetail
     * @param ce the communication type defined in OutputDetail
     * @return the corresponding {@link CommunicationType}
     */
    public static CommunicationType getCommunicationTypeFor( OutputDetail.CommunicationTypeEnum ce ) {
    	return CommunicationType.values()[ce.ordinal()];
    }

    /**
     * get the {@link Program.Runtime} for a runtime defined in ProgramDetail
     * @param re the runtime defined in ProgramDetail
     * @return the corresponding {@link Program.Runtime}
     */
    public static Program.Runtime getRuntimeFor( ProgramDetail.RuntimeEnum re ) {
    	return Program.Runtime.values()[re.ordinal()];
    }

    /**
     * get the {@link ProcessEnvironment} for a ProcessEnvironment defined in ExecutionDetail
     * @param re the ProcessEnvironment defined in ExecutionDetail
     * @return the corresponding {@link ProcessEnvironment}. Default is {@link ProcessEnvironment#DOCKER}
     */
    public static ProcessEnvironment getProcessEnvironmentFor( ExecutionDetail.ProcessEnvironmentEnum pee ) {
    	return pee != null ? ProcessEnvironment.values()[pee.ordinal()] : ProcessEnvironment.DOCKER;
    }

}

