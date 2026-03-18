import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { SnippetsService } from 'src/app/services/snippets.service';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { ROLE } from 'src/app/models/role';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  @Input() title: string = "";
  searchSnippet: string = "";
  user = User.getInstance();
  ROLE = ROLE;

  constructor(public snippetService: SnippetsService, private router: Router) { }

  async ngOnInit() {
    this.searchSnippet = await firstValueFrom(this.snippetService.searchedValue$);
  }

  back() {
    this.router.navigate(["/snippets"]);
  }

  logout() {
    let ok = confirm("Do you really want to log out ? 😟");

    if (ok) {
      localStorage.clear();
      this.router.navigateByUrl('/login');
    }
  }

}
