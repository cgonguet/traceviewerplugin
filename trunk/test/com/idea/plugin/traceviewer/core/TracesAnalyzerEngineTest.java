package com.idea.plugin.traceviewer.core;

import com.intellij.execution.ui.ConsoleViewContentType;
import junit.framework.TestCase;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class TracesAnalyzerEngineTest extends TestCase {

  public void testPrintLine() throws Exception {
    MyEngine engine = new MyEngine(false, false);
    engine.traceFormat = "NEW TRACE - ";
    engine.styles = new ArrayList<TraceStyle>();
    engine.styles.add(new TraceStyle("ERROR.+", Color.red, Color.WHITE, Font.BOLD, true, false));
    engine.styles.add(new TraceStyle("WARNING.+", Color.orange, Color.WHITE, Font.BOLD, true, false));
    engine.styles.add(new TraceStyle("DEBUG.+", Color.black, Color.WHITE, Font.PLAIN, false, true));
    engine.styles.add(new TraceStyle("INFO.*%BR%.*TADA.*", Color.black, Color.WHITE, Font.PLAIN, false, false));
    engine.registerStyles();

    engine.printByBlock = true;

    String text = "NEW TRACE - ERROR - (1) this is an error\n" +
                  "(1.1) in block ERROR\n" +
                  "(1.2) exception here ...\n" +
                  "NEW TRACE - INFO - (2) this is an info\n" +
                  "(2.1) blablabla\n" +
                  "(2.2) TADA\n" +
                  "(2.3) bidibidibidi\n" +
                  "NEW TRACE - WARNING - (3) this is a warning\n" +
                  "(3.1) warning continue at this line\n\n" +
                  "NEW TRACE - DEBUG - (4) this is a debug\n" +
                  "(4.1) in block DEBUG\n" +
                  "NEW TRACE - INFO - (5) this is an info\n" +
                  "NOT A TRACE - ERROR - (6) this is not an error\n";

    BufferedReader reader = new BufferedReader(new StringReader(text));
    TracesAnalyzerEngine.TracesAnalyzer textLoader = engine.getTracesAnalyzer();
    textLoader.countLines();
    while (textLoader.printLine(reader)) {
    }

  }

  public void testVeryLongLine() throws Exception {
    String text = "NEW TRACE - Start\n" +
                  "NEW TRACE - Info 1\n" +
                  "NEW TRACE - Info 2\n" +
                  "NEW TRACE - Info 3\n" +
                  "NEW TRACE - Info 4\n" +
                  "NEW TRACE - Info 5\n" +
                  "NEW TRACE - Info 6\n" +
                  "NEW TRACE - Trace Very Long line: <attributes><aCRpreference>both</aCRpreference><activationInterval>10</activationInterval><activeCallRedirectEnabled>true</activeCallRedirectEnabled><administrativeState>unlocked</administrativeState><auditTime>24</auditTime><auditWindow>10</auditWindow><autoConfigCMDelta>-10</autoConfigCMDelta><autoConfigPWBsrBasedPilotPowerAdjustInterval>120</autoConfigPWBsrBasedPilotPowerAdjustInterval><autoConfigPWIndoorPenetrationLoss>10.0</autoConfigPWIndoorPenetrationLoss><autoConfigPWMaxBSRPowerdBm>13.0</autoConfigPWMaxBSRPowerdBm><autoConfigPWMaxCoverageDistancem>30.0</autoConfigPWMaxCoverageDistancem><autoConfigPWMaxPilotPowerdBm>20.0</autoConfigPWMaxPilotPowerdBm><autoConfigPWMinBSRPowerdBm>0.0</autoConfigPWMinBSRPowerdBm><autoConfigPWMinPilotPowerdBm>-20.0</autoConfigPWMinPilotPowerdBm><autoConfigPWPAdjustmentStepdB>1.0</autoConfigPWPAdjustmentStepdB><autoConfigPWPCPICHPowerIni>-30.0</autoConfigPWPCPICHPowerIni><autoConfigPWPCPICHadjustHysteresisdB>2.0</autoConfigPWPCPICHadjustHysteresisdB><autoConfigPWTargetPilotEcIodB>-14.0</autoConfigPWTargetPilotEcIodB><autoConfigPWTargetPilotRSCPdBm>-100.0</autoConfigPWTargetPilotRSCPdBm><autoConfigPWUeBasedPilotPowerAdjustInterval>120</autoConfigPWUeBasedPilotPowerAdjustInterval><autoConfigProcedureTimer>120</autoConfigProcedureTimer><autoConfigProcedureTimerSqInit>60</autoConfigProcedureTimerSqInit><bCHDecodeGuardTimer>6</bCHDecodeGuardTimer><backhaulClearTimer>210</backhaulClearTimer><bsrPilotPowerAdjustMode>mimBased</bsrPilotPowerAdjustMode><bsrProfileID><unset/></bsrProfileID><cellSearchGuardTimer>6</cellSearchGuardTimer><cipheringEnabled>false</cipheringEnabled><codingScheme>0</codingScheme><dbcMaxRateConvDL>850</dbcMaxRateConvDL><dbcMaxRateConvUL>850</dbcMaxRateConvUL><dbcMaxRateTurboDL>1260</dbcMaxRateTurboDL><dbcMaxRateTurboUL>1260</dbcMaxRateTurboUL><dbcMaxSF>1</dbcMaxSF><defaultPSCEnableFlag>false</defaultPSCEnableFlag><deltPowerReductiondB>3.0</deltPowerReductiondB><enableEthQoS>false</enableEthQoS><enableMTUsizeAdaptation>false</enableMTUsizeAdaptation><enableNormalCallPreemption>true</enableNormalCallPreemption><enableTransportCAC>false</enableTransportCAC><femtoACLenable>true</femtoACLenable><femtoPSCListEnableFlag>false</femtoPSCListEnableFlag><femtoPSCRangeLength>15</femtoPSCRangeLength><femtoPSCReservedIndex>1</femtoPSCReservedIndex><femtoPSCStartRange>0</femtoPSCStartRange><fullnameofNetwork>Paris</fullnameofNetwork><fullnameofNetwork2><unset/></fullnameofNetwork2><hSDPAMeasurementPowerOffset>0.0</hSDPAMeasurementPowerOffset><identityCheckEnabledAutoConfig>true</identityCheckEnabledAutoConfig><interFreqNumCellsMeasReport>1</interFreqNumCellsMeasReport><itfBSRNTPqosMapping>ef</itfBSRNTPqosMapping><itfBSROAMqosMapping>af21</itfBSROAMqosMapping><lMCrouterMACAddressTestLocationWeight>0</lMCrouterMACAddressTestLocationWeight><lMCrouterMACAddressTestMovementWeight>50</lMCrouterMACAddressTestMovementWeight><lMCrouterMACAddressTestTestComplete>false</lMCrouterMACAddressTestTestComplete><lMCrouterMACAddressTestTestScore>0</lMCrouterMACAddressTestTestScore><lMCthresholdLevelLocationWeight>50</lMCthresholdLevelLocationWeight><lMCthresholdLevelMovementWeight>100</lMCthresholdLevelMovementWeight><lMCtraceroutePathTestLocationWeight>0</lMCtraceroutePathTestLocationWeight><lMCtraceroutePathTestMovementWeight>50</lMCtraceroutePathTestMovementWeight><lMCtraceroutePathTestTestComplete>false</lMCtraceroutePathTestTestComplete><lMCtraceroutePathTestTestScore>0</lMCtraceroutePathTestTestScore><lMCvisibleMacroCellsTestLocationWeight>0</lMCvisibleMacroCellsTestLocationWeight><lMCvisibleMacroCellsTestMovementWeight>50</lMCvisibleMacroCellsTestMovementWeight><lMCvisibleMacroCellsTestTestComplete>false</lMCvisibleMacroCellsTestTestComplete><lMCvisibleMacroCellsTestTestScore>0</lMCvisibleMacroCellsTestTestScore><locationName><unset/></locationName><maxSizeHistoryLog>512</maxSizeHistoryLog><nMO>nMOI</nMO><netkListGuardConfigTimer>5000</netkListGuardConfigTimer><oVCFPCPMeasUpd>200</oVCFPCPMeasUpd><oVCFPCPOcfMin>2</oVCFPCPOcfMin><oVCFPCPOvlRedistInt>1</oVCFPCPOvlRedistInt><oVCFPCPOvlRedistMax>2</oVCFPCPOvlRedistMax><oVCFPCPThrCpuFpcpUmts>90</oVCFPCPThrCpuFpcpUmts><overrideLocationCheck>false</overrideLocationCheck><ovsIuCSVoice>125</ovsIuCSVoice><publicUserRejectCause>13</publicUserRejectCause><rABInactivityTimer>120</rABInactivityTimer><reservedRBGWDLBW>0</reservedRBGWDLBW><reservedRBGWULBW>0</reservedRBGWULBW><sampleGaugeCountersTimer>5000</sampleGaugeCountersTimer><selfOptMissedCount>0</selfOptMissedCount><shortnameofNetwork>111</shortnameofNetwork><shortnameofNetwork2><unset/></shortnameofNetwork2><sparePara1><unset/></sparePara1><sparePara2><unset/></sparePara2><sparePara3><unset/></sparePara3><sparePara4><unset/></sparePara4><sparePara5><unset/></sparePara5><tCCOSupervision>60</tCCOSupervision><tHandoverGuard>5</tHandoverGuard><thresholdFactor>20</thresholdFactor><timeZone>0</timeZone><uEtoBSRReportingCriteriaRSSITimetoTrigger>timetotrigger20</uEtoBSRReportingCriteriaRSSITimetoTrigger><uEtoBSRReportingCriteriaReportingInterval>reportinginterval1000</uEtoBSRReportingCriteriaReportingInterval><uTRANDrxCycleCoefficient>3</uTRANDrxCycleCoefficient><ueBasedPilotPowerAdjustMode>disable</ueBasedPilotPowerAdjustMode><umtsNtwkListenEnableFlag>true</umtsNtwkListenEnableFlag><umtsOpenSearchEnableFlag>true</umtsOpenSearchEnableFlag><userLabel><unset/></userLabel></attributes>" +
                  "suite <attributes><aCRpreference>both</aCRpreference><activationInterval>10</activationInterval><activeCallRedirectEnabled>true</activeCallRedirectEnabled><administrativeState>unlocked</administrativeState><auditTime>24</auditTime><auditWindow>10</auditWindow><autoConfigCMDelta>-10</autoConfigCMDelta><autoConfigPWBsrBasedPilotPowerAdjustInterval>120</autoConfigPWBsrBasedPilotPowerAdjustInterval><autoConfigPWIndoorPenetrationLoss>10.0</autoConfigPWIndoorPenetrationLoss><autoConfigPWMaxBSRPowerdBm>13.0</autoConfigPWMaxBSRPowerdBm><autoConfigPWMaxCoverageDistancem>30.0</autoConfigPWMaxCoverageDistancem><autoConfigPWMaxPilotPowerdBm>20.0</autoConfigPWMaxPilotPowerdBm><autoConfigPWMinBSRPowerdBm>0.0</autoConfigPWMinBSRPowerdBm><autoConfigPWMinPilotPowerdBm>-20.0</autoConfigPWMinPilotPowerdBm><autoConfigPWPAdjustmentStepdB>1.0</autoConfigPWPAdjustmentStepdB><autoConfigPWPCPICHPowerIni>-30.0</autoConfigPWPCPICHPowerIni><autoConfigPWPCPICHadjustHysteresisdB>2.0</autoConfigPWPCPICHadjustHysteresisdB><autoConfigPWTargetPilotEcIodB>-14.0</autoConfigPWTargetPilotEcIodB><autoConfigPWTargetPilotRSCPdBm>-100.0</autoConfigPWTargetPilotRSCPdBm><autoConfigPWUeBasedPilotPowerAdjustInterval>120</autoConfigPWUeBasedPilotPowerAdjustInterval><autoConfigProcedureTimer>120</autoConfigProcedureTimer><autoConfigProcedureTimerSqInit>60</autoConfigProcedureTimerSqInit><bCHDecodeGuardTimer>6</bCHDecodeGuardTimer><backhaulClearTimer>210</backhaulClearTimer><bsrPilotPowerAdjustMode>mimBased</bsrPilotPowerAdjustMode><bsrProfileID><unset/></bsrProfileID><cellSearchGuardTimer>6</cellSearchGuardTimer><cipheringEnabled>false</cipheringEnabled><codingScheme>0</codingScheme><dbcMaxRateConvDL>850</dbcMaxRateConvDL><dbcMaxRateConvUL>850</dbcMaxRateConvUL><dbcMaxRateTurboDL>1260</dbcMaxRateTurboDL><dbcMaxRateTurboUL>1260</dbcMaxRateTurboUL><dbcMaxSF>1</dbcMaxSF><defaultPSCEnableFlag>false</defaultPSCEnableFlag><deltPowerReductiondB>3.0</deltPowerReductiondB><enableEthQoS>false</enableEthQoS><enableMTUsizeAdaptation>false</enableMTUsizeAdaptation><enableNormalCallPreemption>true</enableNormalCallPreemption><enableTransportCAC>false</enableTransportCAC><femtoACLenable>true</femtoACLenable><femtoPSCListEnableFlag>false</femtoPSCListEnableFlag><femtoPSCRangeLength>15</femtoPSCRangeLength><femtoPSCReservedIndex>1</femtoPSCReservedIndex><femtoPSCStartRange>0</femtoPSCStartRange><fullnameofNetwork>Paris</fullnameofNetwork><fullnameofNetwork2><unset/></fullnameofNetwork2><hSDPAMeasurementPowerOffset>0.0</hSDPAMeasurementPowerOffset><identityCheckEnabledAutoConfig>true</identityCheckEnabledAutoConfig><interFreqNumCellsMeasReport>1</interFreqNumCellsMeasReport><itfBSRNTPqosMapping>ef</itfBSRNTPqosMapping><itfBSROAMqosMapping>af21</itfBSROAMqosMapping><lMCrouterMACAddressTestLocationWeight>0</lMCrouterMACAddressTestLocationWeight><lMCrouterMACAddressTestMovementWeight>50</lMCrouterMACAddressTestMovementWeight><lMCrouterMACAddressTestTestComplete>false</lMCrouterMACAddressTestTestComplete><lMCrouterMACAddressTestTestScore>0</lMCrouterMACAddressTestTestScore><lMCthresholdLevelLocationWeight>50</lMCthresholdLevelLocationWeight><lMCthresholdLevelMovementWeight>100</lMCthresholdLevelMovementWeight><lMCtraceroutePathTestLocationWeight>0</lMCtraceroutePathTestLocationWeight><lMCtraceroutePathTestMovementWeight>50</lMCtraceroutePathTestMovementWeight><lMCtraceroutePathTestTestComplete>false</lMCtraceroutePathTestTestComplete><lMCtraceroutePathTestTestScore>0</lMCtraceroutePathTestTestScore><lMCvisibleMacroCellsTestLocationWeight>0</lMCvisibleMacroCellsTestLocationWeight><lMCvisibleMacroCellsTestMovementWeight>50</lMCvisibleMacroCellsTestMovementWeight><lMCvisibleMacroCellsTestTestComplete>false</lMCvisibleMacroCellsTestTestComplete><lMCvisibleMacroCellsTestTestScore>0</lMCvisibleMacroCellsTestTestScore><locationName><unset/></locationName><maxSizeHistoryLog>512</maxSizeHistoryLog><nMO>nMOI</nMO><netkListGuardConfigTimer>5000</netkListGuardConfigTimer><oVCFPCPMeasUpd>200</oVCFPCPMeasUpd><oVCFPCPOcfMin>2</oVCFPCPOcfMin><oVCFPCPOvlRedistInt>1</oVCFPCPOvlRedistInt><oVCFPCPOvlRedistMax>2</oVCFPCPOvlRedistMax><oVCFPCPThrCpuFpcpUmts>90</oVCFPCPThrCpuFpcpUmts><overrideLocationCheck>false</overrideLocationCheck><ovsIuCSVoice>125</ovsIuCSVoice><publicUserRejectCause>13</publicUserRejectCause><rABInactivityTimer>120</rABInactivityTimer><reservedRBGWDLBW>0</reservedRBGWDLBW><reservedRBGWULBW>0</reservedRBGWULBW><sampleGaugeCountersTimer>5000</sampleGaugeCountersTimer><selfOptMissedCount>0</selfOptMissedCount><shortnameofNetwork>111</shortnameofNetwork><shortnameofNetwork2><unset/></shortnameofNetwork2><sparePara1><unset/></sparePara1><sparePara2><unset/></sparePara2><sparePara3><unset/></sparePara3><sparePara4><unset/></sparePara4><sparePara5><unset/></sparePara5><tCCOSupervision>60</tCCOSupervision><tHandoverGuard>5</tHandoverGuard><thresholdFactor>20</thresholdFactor><timeZone>0</timeZone><uEtoBSRReportingCriteriaRSSITimetoTrigger>timetotrigger20</uEtoBSRReportingCriteriaRSSITimetoTrigger><uEtoBSRReportingCriteriaReportingInterval>reportinginterval1000</uEtoBSRReportingCriteriaReportingInterval><uTRANDrxCycleCoefficient>3</uTRANDrxCycleCoefficient><ueBasedPilotPowerAdjustMode>disable</ueBasedPilotPowerAdjustMode><umtsNtwkListenEnableFlag>true</umtsNtwkListenEnableFlag><umtsOpenSearchEnableFlag>true</umtsOpenSearchEnableFlag><userLabel><unset/></userLabel></attributes>\n" +
                  "NEW TRACE - End";

    MyEngine engine = new MyEngine(false, false);
    engine.traceFormat = "NEW TRACE - ";
    engine.styles = new ArrayList<TraceStyle>();
    engine.styles.add(new TraceStyle("(Start|End).+", Color.red, Color.WHITE, Font.BOLD, true, false));
    engine.styles.add(new TraceStyle("Trace.+", Color.black, Color.WHITE, Font.PLAIN, false, true));
    engine.registerStyles();

    engine.printByBlock = true;
    BufferedReader reader = new BufferedReader(new StringReader(text));
    TracesAnalyzerEngine.TracesAnalyzer textLoader = engine.getTracesAnalyzer();
    textLoader.countLines();
    while (textLoader.printLine(reader)) {
    }
  }

  class MyEngine extends TracesAnalyzerEngine {

    public MyEngine(boolean resume, boolean filter)
            throws IOException {
      super(null, null, null, resume, filter);
    }


    TracesAnalyzer getTracesAnalyzer() {
      return new TracesAnalyzer(new TraceWorker() {

        public void print(String str, ConsoleViewContentType style) {
          System.out.println("PRINT WITH STYLE: " + style);
          System.out.println("--> " + str);
        }

        public void ignore(String str) {
          System.out.println("IGNORE: " + str);
        }
      }, new ProgressIndicatorManager() {

        public void init() {
        }

        public void setProgress(double fraction) {
          System.out.println("progress = " + fraction);
        }

        public boolean isCanceled() {
          return false;
        }
      }) {

        void countLines() throws IOException {
          maxLine = 14;
        }
      };
    }

  }


  public void testRegexpByBlock() throws Exception {
    String block = "NEW TRACE - INFO - begin here\n" +
                   "continue and print this: \n" +
                   "TADA TADA\n" +
                   "...\n";

    String blockline = block.replaceAll("\n", "%BR%");
    System.out.println("blockline = " + blockline);
    if (blockline.matches("^NEW TRACE - INFO - .+%BR%TADA TADA.*")) {
      System.out.println("match");
    } else {
      System.out.println("no");
    }
  }

}
