//package HandyTests
//
//object SWITest {
//
//  def main(args: Array[String]): Unit = {
//
//    import org.jpl7.JPL
//    import org.jpl7.fli.Prolog
//    import java.util
//    if (System.getenv("SWI_HOME_DIR") != null || System.getenv("SWI_EXEC_FILE") != null || System.getenv("SWIPL_BOOT_FILE") != null) {
//      val init_swi_config = String.format("%s %s %s -g true -q --no-signals --no-packs", if (System.getenv("SWI_EXEC_FILE") == null) "swipl"
//      else System.getenv("SWI_EXEC_FILE"), if (System.getenv("SWIPL_BOOT_FILE") == null) ""
//      else String.format("-x %s", System.getenv("SWIPL_BOOT_FILE")), if (System.getenv("SWI_HOME_DIR") == null) ""
//      else String.format("--home=%s", System.getenv("SWI_HOME_DIR")))
//      System.out.println(String.format("\nSWIPL initialized with: %s", init_swi_config))
//      JPL.setDefaultInitArgs(init_swi_config.split("\\s+")) // initialize SWIPL engine
//
//    }
//    else System.out.println("No explicit initialization done: no SWI_HOME_DIR, SWI_EXEC_FILE, or SWIPL_BOOT_FILE defined")
//
//    //JPL.setTraditional(); // most often not used
//    JPL.init
//    System.out.println("Prolog engine actual init args: " + Prolog.get_actual_init_args)
//
//  }
//
//}
