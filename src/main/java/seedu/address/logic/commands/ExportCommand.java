package seedu.address.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Address;
import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.CreateContactGroupRequest;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.Photo;
import com.google.api.services.people.v1.model.UserDefined;
import com.google.common.eventbus.Subscribe;

import seedu.address.commons.auth.GoogleApiAuth;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleApiAuthServiceCredentialsSetupCompleted;
import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

public class ExportCommand extends GoogleCommand {
    public static final String COMMAND_WORD = "export";

    //Scope includes write access to a users' Google Contacts
    public static final String ACCESS_SCOPE = "https://www.googleapis.com/auth/contacts";

    private PeopleService peopleService;


    public ExportCommand(String commandWord, String accessScope) throws IOException{
        super(COMMAND_WORD, ACCESS_SCOPE);
    }

    @Override
    @Subscribe
    protected void handleAuthenticationSuccessEvent(GoogleAuthenticationSuccessEvent event) {
        if (!commandTypeCheck(event.getCommandType())) {
            return;
        }
        //set up credentials
        setupCredentials(event.getAuthCode());


        //Conversion calls
        List<ReadOnlyPerson> docPersonList = model.getAddressBook().getPersonList();
        List<com.google.api.services.people.v1.model.Person> googlePersonList =
                GooglePersonConverterUtil.listDocToGooglePersonConversion(docPersonList);

        //HTTP calls
        for (com.google.api.services.people.v1.model.Person p : googlePersonList) {
            try {
                peopleService.people().createContact(p).execute();
            } catch (IOException E) {
                System.out.println(E);
            }
        }
    }
    private boolean commandTypeCheck(String inputCommandType) {
        return commandType.equals("GOOGLE_export");
    }

    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new GoogleAuthRequestEvent(authService));
        return null;
    }

    /**
     * Event listener for successful setup of authService's credentials
     *
     */
    @Subscribe
    private void handleGoogleApiAuthServiceCredentialsSetupComplete(GoogleApiAuthServiceCredentialsSetupCompleted event) {
        peopleService = new PeopleService.Builder(httpTransport, jsonFactory, authService.getCredential())
                .setApplicationName("CS2103T - Doc")
                .build();

        List<ReadOnlyPerson> docPersonList = model.getAddressBook().getPersonList();
        List<com.google.api.services.people.v1.model.Person> googlePersonList = new ArrayList<>();
        listConvertDocToGooglePerson(docPersonList, googlePersonList);

//        CreateContactGroupRequest newRequest = new CreateContactGroupRequest();
//        ContactGroup newGroup = new ContactGroup();
//        newGroup.setName("DoC Contacts");
//        newRequest.setContactGroup(new ContactGroup());


        for (com.google.api.services.people.v1.model.Person p : googlePersonList) {
            try {
                peopleService.people().createContact(p).execute();
            } catch (IOException E) {
                System.out.println(E);
            }
        }
    }
//
//    private void listConvertDocToGooglePerson(List<ReadOnlyPerson>docList,
//                                              List<com.google.api.services.people.v1.model.Person> googleList) {
//        for (ReadOnlyPerson p : docList) {
//            com.google.api.services.people.v1.model.Person tempPerson =
//                    new com.google.api.services.people.v1.model.Person();
//
//            Name googleName = new Name().setDisplayName(p.getName().fullName);
//            googleName.setGivenName(p.getName().fullName);
////            googleName.setPhoneticFullName(p.getName().fullName);
//            PhoneNumber googleNumber = new PhoneNumber().setValue(p.getPhone().value);
//            EmailAddress googleEmail = new EmailAddress().setValue(p.getEmail().value);
//            Address googleAddress = new Address().setFormattedValue(p.getAddress().value);
//
//
//            List<Name> googleNameList = makeListFromOne(googleName);
//            List<PhoneNumber> googlePhoneNumberList = makeListFromOne(googleNumber);
//            List<EmailAddress> googleEmailAddressList = makeListFromOne(googleEmail);
//            List<Address> googleAddressList = makeListFromOne(googleAddress);
//            List<UserDefined> googleTagList = new ArrayList<UserDefined>();
//            List<Photo> googlePhotoList = new ArrayList<Photo>();
//            //set tags
//            for (Tag t : p.getTags()) {
//                UserDefined tempGoogleTag = new UserDefined();
//                tempGoogleTag.setKey("tag");
//                tempGoogleTag.setValue(t.tagName);
//                googleTagList.add(tempGoogleTag);
//            }
//            //set photo
//            googlePhotoList.add(new Photo().setUrl(p.getProfilePic().source));
//
//            //finalize Google Person
//            tempPerson.setNames(googleNameList);
//            tempPerson.setPhoneNumbers(googlePhoneNumberList);
//            tempPerson.setEmailAddresses(googleEmailAddressList);
//            tempPerson.setAddresses(googleAddressList);
//            tempPerson.setUserDefined(googleTagList);
////            tempPerson.setPhotos(googlePhotoList);
//            googleList.add(tempPerson);
//        }
//    }


}