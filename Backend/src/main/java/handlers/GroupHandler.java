package handlers;

import dto.Group;
import dto.User;
import exceptions.*;
import services.FileService;
import services.UserService;

import java.util.List;
import java.util.Scanner;

public class GroupHandler {
    private final Scanner in = new Scanner(System.in);
    private final FileService fileService;
    private final UserService userService;

    public GroupHandler(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    public void displaySpecificGroupFromFile() {
        System.out.println(getGroup());
    }

    public void addGroup() {
        System.out.print("Enter group name: ");
        String groupName = in.nextLine();
        validateGroupUserInput(groupName);

        List<Group> loadedGroups = loadAllGroups();
        validateFindFileGroupName(loadedGroups, groupName);

        Group newGroup = new Group(groupName);
        saveGroup(newGroup);
    }

    public void addNewUserToGroup() {
        Group group = getGroup();
        List<User> users = group.getGroupMembers();
        User newUser = userService.createUser();

        if (isUserAlreadyInGroup(users, newUser)) {
            System.out.println("This user already exists...");
            return;
        }
        users.add(newUser);
        saveGroup(group);
        System.out.println("User successfully added and group saved!");
    }

    public void addNewGradeToUserAndSaveToFile() {
        Group group = getGroup();
        userService.addGradeUser(group);
        saveGroup(group);
    }

    public void deleteUserFromSpecificGroup() {
        Group group = getGroup();
        userService.deleteUser(group);
        saveGroup(group);
    }

    public void displayUserFromSpecificGroup() {
        System.out.println(getSpecificUser());
    }

    public void updateUserGradeAndSaveToFile() {
        Group specificGroup = getGroup();
        userService.updateUserGrade(specificGroup);
        saveGroup(specificGroup);
    }

    public void deleteUserGrade() {
        Group specificGroup = getGroup();
        userService.deleteUserGrade(specificGroup);
        saveGroup(specificGroup);
    }

    private void saveGroup(Group group){
        fileService.saveGroupToFile(group);
    }

    private Group loadGroup(String searchedName) {
        return fileService.loadGroup(searchedName);
    }

    private List<Group> loadAllGroups() {
        return fileService.loadAllGroups();
    }

    private boolean isUserAlreadyInGroup(List<User> users, User newUser) {
        return users.stream().anyMatch(user -> user.getUsername().equals(newUser.getUsername()));
    }

    private boolean groupNameHandler(Group group, String searchedGroupName) {
        return group == null || !group.getGroupName().equals(searchedGroupName);
    }

    private void validateGroupUserInput(String groupName) {
        if (groupName.isEmpty()) {
            throw new InvalidUserInput("It must contain at least a letter");
        }
    }

    private void validateFindFileGroupName(List<Group> loadedGroups, String groupName) {
        if (doesGroupExist(loadedGroups, groupName)) {
            throw new GroupAlreadyExists("Group with this name already exists.");
        }
    }

    private boolean doesGroupExist(List<Group> loadedGroups, String nameOfNewGroup) {
        return loadedGroups.stream()
                .anyMatch(group -> group.getGroupName().equalsIgnoreCase(nameOfNewGroup));
    }

    private User getSpecificUser() {
        Group specificGroup = getGroup();
        return getUserFromGroup(specificGroup);
    }

    private Group getGroup() {
        System.out.print("Enter group name: ");
        String searchedGroupName = in.nextLine();
        Group group = loadGroup(searchedGroupName);

        if (groupNameHandler(group, searchedGroupName)) {
            throw new GroupNotFoundException("Could not find group with the name: " + searchedGroupName);
        }

        return group;
    }

    private User getUserFromGroup(Group loadedGroup) {
        return userService.getUserFromGroup(loadedGroup);
    }
}
