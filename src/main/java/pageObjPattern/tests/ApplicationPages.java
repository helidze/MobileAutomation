package pageObjPattern.tests;

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pageObjPattern.basePage.MainPage;

public class ApplicationPages extends MainPage {

    protected static final Logger LOG = Logger.getLogger(ApplicationPages.class);
    public BasicTestsExecutor basicTestsExecutor;

    public ApplicationPages(WebDriver webDriver) {
        super(webDriver);
        basicTestsExecutor = new BasicTestsExecutor(webDriver);
    }

    /*public UnsubscribePage goToUnsubscribePage(String unsubscribeLink) {
        goToPage(unsubscribeLink);
        return new UnsubscribePage(webDriver);
    }

    public ContactViewListActionPage goToContactsPage() {
        String url = AppConfig.getStartUrl() + MemberUri.CONTACT_VIEW_LIST_ACTION.getUri();
        goToPage(url);
        LOG.info("Go to Contacts page. OPEN URL: '" + url + "'.");
        return new ContactViewListActionPage(webDriver);
    }*/

    /*public TaskManagementPage goToTasksViewPage(long taskDefinitionId) {
        String url = AppConfig.getStartUrl() + "/app/action/tasks/view/" + taskDefinitionId;
        goToPage(url);
        LOG.info("Go to task view page for TDID '" + taskDefinitionId + "'. OPEN URL: '" + url + "'.");
        return new TaskManagementPage(webDriver);
    }

    public TaskDefinitionBasicsPage goToEditTDPage(long taskDefinitionId) {
        String url = AppConfig.getStartUrl() + "/app/action/main/tasks/definitions/basics/" + taskDefinitionId;
        goToPage(url);
        LOG.info("Go to edit page of task definition with ID: '" + taskDefinitionId + "'. OPEN URL: '" + url + "'.");
        return new TaskDefinitionBasicsPage(webDriver);
    }

    public ReportQuestionsPage goToReportPage(long surveyId, long reportId) {
        String url = AppConfig.getStartUrl() + MemberUri.QUESTIONS_DO.getUri() + "?reportID=" + reportId + "&surveyId=" + surveyId;
        goToPage(url);
        LOG.info("Go to report page of survey with ID: '" + surveyId + ", report Id: '" + reportId + "'. OPEN URL: '" + url + "'.");
        ReportQuestionsPage reportQuestionsPage = new ReportQuestionsPage(webDriver);
        reportQuestionsPage.waitForRunButtonEnabled();
        return reportQuestionsPage;
    }

    public QuestionListPage goToQuestionListPage(long surveyId) {
        String url = AppConfig.getStartUrl() + MemberUri.QUESTIONS_LIST_DO.getUri() + "?SLSelect=" + surveyId;
        goToPage(url);
        LOG.info("Go to question list page of survey with ID: '" + surveyId + "'. OPEN URL: '" + url + "'.");
        QuestionListPage questionListPage = new QuestionListPage(webDriver);
        questionListPage.waitForLaunchButton();
        questionListPage.waitForAjax(5);
        return questionListPage;
    }

    public FormPage goToConstructorPage(long surveyId) {
        String url = AppConfig.getStartUrl() + "/app/action/layout/constructor/view/" + surveyId;
        goToPage(url);
        LOG.info("Go to constructor page of form with ID: '" + surveyId + "'. OPEN URL: '" + url + "'.");
        return new FormPage(webDriver);
    }

    public VotingPage goToVotingPageByUrl(String link) {
        goToPage(link);
        LOG.info("Go to voting page of form. OPEN URL: '" + link + "'.");
        return new VotingPage(webDriver);
    }

    protected PluginsPage goToPluginsPageForSurvey(long surveyId) {
        goToPage(AppConfig.getStartUrl() + MemberUri.PLUGIN_ACTION.getUri() + "?sid=" + surveyId);
        return new PluginsPage(webDriver);
    }

    protected VotingPage goToVotingPageByVotingURL(String link) {
        goToPage(link);
        return new VotingPage(webDriver);
    }

    protected ReportByRespondentPage goToReportByRespondentPage(String link) {
        goToPage(link);
        return new ReportByRespondentPage(webDriver);
    }

    public TaskManagementPage goToTaskManagementPage(long taskDefinition) {
        goToPage(AppConfig.getStartUrl() + "/app/action/tasks/view/" + taskDefinition);
        return new TaskManagementPage(webDriver);
    }

    public ReportsPage goToReportDashboardAndSelectReport(long surveyId, long reportId) {
        goToPage(AppConfig.getStartUrl() + "/app/action/report/Home/view/s" + surveyId + "/?rid=" + reportId);
        return new ReportsPage(webDriver);
    }

    public OfflineLoginPage goToOfflinePortalLoginPage(long portalId) {
        goToPage(AppConfig.getStartUrl() + "/offline/#" + portalId);
        return new OfflineLoginPage(webDriver);
    }

    public OfflineLoginPage goToOnlinePortalLoginPage(long portalId) {
        goToPage(AppConfig.getStartUrl() + "/portal/#" + portalId);
        return new OfflineLoginPage(webDriver);
    }

    public ListOfReportsPage goToNewReportPage() {
        webDriver.get(AppConfig.getStartUrl() + "/reports");
        basicTestsExecutor.waitForPageLoaded(webDriver);
        return new ListOfReportsPage(webDriver);
    }

    public EditReportPage goToEditReportPage(long reportId) {
        String url = AppConfig.getStartUrl() + "/reports/edit/" + reportId;
        LOG.info("Open edit report page " + url);
        webDriver.get(url);
        return new EditReportPage(webDriver);
    }

    public RunReportPage goToRunReportPage(String reportLink) {
        LOG.info("Open run report page " + reportLink);
        webDriver.get(reportLink);
        return new RunReportPage(webDriver);
    }*/

    private void goToPage(String url) {
        webDriver.get(url);
        basicTestsExecutor.waitForPageLoaded(webDriver);
        basicTestsExecutor.waitForAjax(10);
    }

}
