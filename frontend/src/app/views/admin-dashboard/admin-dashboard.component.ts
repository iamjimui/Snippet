import { Component } from '@angular/core';
import { UsersService } from '../../services/users.service';
import { TagsService } from 'src/app/services/tags.service';
import { LanguagesService } from 'src/app/services/languages.service';
import { User } from 'src/app/models/user.model';
import { TagModel } from 'src/app/models/tags.model';
import { LanguageModel } from 'src/app/models/languages.model';
import { ROLE } from 'src/app/models/role';
import { UserModel } from 'src/app/models/users.model';
import { AlertService } from 'src/app/services/alert.service';
import { AlertType } from 'src/app/models/alert.model';
@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent {

  user = User.getInstance();
  searchedTag = "";
  ROLE = ROLE;

  constructor(public userService: UsersService, public tagService: TagsService, public languageService: LanguagesService, private alertService: AlertService) { }

  ngOnInit() {
    this.userService.getAllFromAdmin();
    this.tagService.getAllFromAdmin();
    this.languageService.getAllFromAdmin();
  }

  addTag() {
    let tag = prompt("Add new tag: ");
    if (tag) {
      this.tagService.add(tag, localStorage.getItem("token")!);
    }
  }

  delete(tag: TagModel) {
    let ok = confirm("Do you really want to delete this tag ?");
    if (ok) {
      this.tagService.deleteTag(tag.id, localStorage.getItem("token")!);
    }
  }

  edit(tag: TagModel) {
    let newTag = prompt("Edit tag: ", tag.name);
    if (newTag) {
      this.tagService.updateTag(tag.id, newTag, localStorage.getItem("token")!);
    }
  }

  addLang() {
    let lang = prompt("Add new language: ");
    if (lang) {
      this.languageService.add(lang, localStorage.getItem("token")!);
    }
  }

  deleteLang(lang: LanguageModel) {
    let ok = confirm("Do you really want to delete this language ?");
    if (ok) {
      this.languageService.deleteLanguage(lang.id, localStorage.getItem("token")!);
    }
  }

  editLang(lang: LanguageModel) {
    let newLang = prompt("Edit language: ", lang.name);
    if (newLang) {
      this.languageService.updateLanguage(lang.id, newLang, localStorage.getItem("token")!);
    }
  }

  updateFormUser(event: any) {
    event.preventDefault();
    if (event.submitter.name === "updateUser") {
      if (event.target.user_username.value !== "" && event.target.user_role.value !== "") {
        console.log(event.submitter.name);
        if (event.target.user_password.value !== "") {
          var updatedUser = {
            username: event.target.user_username.value,
            password: event.target.user_password.value,
            role: event.target.user_role.value
          }
          console.log("with password");
          this.userService.updateUser(event.target.user_id.value, updatedUser, localStorage.getItem("token")!);
        } else {
          var updatedUserWithoutPassword = {
            username: event.target.user_username.value,
            role: event.target.user_role.value
          }
          console.log("without password");
          this.userService.updateUser(event.target.user_id.value, updatedUserWithoutPassword, localStorage.getItem("token")!);
        }
      }
    }
  }

  deleteUser(user: UserModel) {
    let ok = confirm("Do you really want to delete this user ? ");
    if (ok) {
      this.userService.deleteUser(user.id, localStorage.getItem("token")!);
    }
  }

  changePassword(user: UserModel) {
    let newPassword = prompt("Enter new password: ");
    if (newPassword) {
      var updatedUser = {
        username: user.username,
        password: newPassword,
        role: user.role
      }
      this.userService.updateUser(user.id, updatedUser, localStorage.getItem("token")!);
      this.alertService.show(AlertType.success, "Password updated");
    }
  }

  changeRole(user: UserModel) {
    if (user.role === ROLE.ROLE_ADMIN) {
      var updatedUserWithoutPassword = {
        username: user.username,
        role: ROLE.ROLE_USER
      }
      this.userService.updateUser(user.id, updatedUserWithoutPassword, localStorage.getItem("token")!);
    } else {
      var updatedUserWithoutPassword = {
        username: user.username,
        role: ROLE.ROLE_ADMIN
      }
      this.userService.updateUser(user.id, updatedUserWithoutPassword, localStorage.getItem("token")!);
    }
    this.alertService.show(AlertType.success, "Role changed");
  }

  deleteFormUser(event: any) {
    event.preventDefault();
    const user_id = event.target.user_id.value;
    this.userService.deleteUser(user_id, localStorage.getItem("token")!);
  }

  submitFormTag(event: any) {
    event.preventDefault();
    const tag = event.target;
    this.tagService.add(tag.name.value, localStorage.getItem("token")!);
  }



}
