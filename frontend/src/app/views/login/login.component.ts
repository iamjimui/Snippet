import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AlertType } from 'src/app/models/alert.model';
import { AlertService } from 'src/app/services/alert.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = "";
  password = "";
  loading = false;

  constructor(private authService: AuthService, private alertService: AlertService, private router: Router) { }

  async login() {
    try {
      if (!this.username || !this.password)
        return;

      this.loading = true;
      await this.authService.login(this.username, this.password);
      this.loading = false;
      this.router.navigateByUrl('snippets');
      this.alertService.show(AlertType.success, `WELCOME ${this.username} 👊`);
    } catch (e: any) {
      this.alertService.show(AlertType.danger, e.error.message);
      this.loading = false;
    }
  }

}
