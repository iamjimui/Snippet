import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AlertType } from 'src/app/models/alert.model';
import { AlertService } from 'src/app/services/alert.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  username = "";
  password = "";
  confirmPassword = "";
  loading = false;


  constructor(private authService: AuthService, private alertService: AlertService, private router: Router) { }

  async register() {
    try {
      if (!this.username || !this.password || !this.confirmPassword)
        return;

      if (this.password !== this.confirmPassword) {
        this.alertService.show(AlertType.danger, "Password confirmation failed.");
        return;
      }

      this.loading = true;
      await this.authService.register(this.username, this.password);
      this.loading = false;
      this.router.navigateByUrl('snippets');
      this.alertService.show(AlertType.success, `WELCOME ${this.username} 👊`);
    } catch (e: any) {
      this.loading = false;
      this.alertService.show(AlertType.danger, e.error.message);
    }
  }

}
