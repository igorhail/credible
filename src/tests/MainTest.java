package tests;

import generic.ChromeTestCase;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import steps.GeneralSteps;
import steps.PersonalLoanRequestSteps;
import steps.ProfileSteps;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MainTest extends ChromeTestCase {

  @Test
  public void newTest() {
    GeneralSteps generalSteps = new GeneralSteps();
    generalSteps.openPageAndDragNDrop();
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test(dataProvider = "getParameters")
  public void test(String rate, String name, String domain, String pass) {
    GeneralSteps generalSteps = new GeneralSteps();
    generalSteps.openStartPage();
    generalSteps.getToPersonalLoan();
    generalSteps.findMyRate(rate);

    PersonalLoanRequestSteps personalLoanRequestSteps = new PersonalLoanRequestSteps();
    ArrayList<String> errorList = new ArrayList<String>();
    errorList = personalLoanRequestSteps.verifyPersonalHeader(errorList);
    personalLoanRequestSteps.fillAllFields();
    errorList = personalLoanRequestSteps.verifyAnnualTip(errorList);
    ProfileSteps profileSteps = personalLoanRequestSteps.clickContinue();

    errorList = profileSteps.verifyPersonalHeader(errorList);
    profileSteps.fillAllFields();
    errorList = profileSteps.verifyCitizenTip(errorList);
    errorList = profileSteps.verifySsnTip(errorList);
    errorList = profileSteps.verifyPhoneTip(errorList);

    String email = profileSteps.getEmail(name, domain);
    profileSteps.fillEmailAndPass(email, pass);
    profileSteps.clickSubmit();

    errorList = profileSteps.verifySpinnerIsShown(errorList);

    Assert.assertTrue(errorList.isEmpty(), errorList.toString());
  }

  @DataProvider(name = "getParameters")
  public Object[][] getParameters(ITestContext context) {
    Map<String, String> datas = context.getCurrentXmlTest().getLocalParameters();
    Set<Map.Entry<String, String>> entrySet = datas.entrySet();

    Object[][] returnValues = new Object[datas.size()][entrySet.iterator().next().getValue().split(",").length];
    int i = 0;
    for (Map.Entry<String, String> entry : entrySet) {
      String[] parameters = entry.getValue().split(",");
      for (int n = 0; n < parameters.length; n++) {
        returnValues[i][n] = parameters[n].trim();
      }
      i++;
    }
    return returnValues;
  }
}
