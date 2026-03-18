import { NgModule, createComponent } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './views/home/home.component';
import { AddComponent } from './views/add/add.component';
import { DetailsComponent } from './views/details/details.component';
import { EditComponent } from './views/edit/edit.component';
import { RegisterComponent } from './views/register/register.component';
import { authGuard } from './guards/auth.guard';
import { LoginComponent } from './views/login/login.component';
import { AdminDashboardComponent } from './views/admin-dashboard/admin-dashboard.component';
import { adminGuard } from './guards/admin.guard';
import { AdminCommentsComponent } from './views/admin-comments/admin-comments.component';

const routes: Routes = [
  {
    path: "register",
    component: RegisterComponent,
  },
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "snippets",
    component: HomeComponent,
    canActivate: [authGuard]
  },
  {
    path: "add",
    component: AddComponent,
    canActivate: [authGuard]
  },
  {
    path: "snippet/:id",
    component: DetailsComponent,
    canActivate: [authGuard]
  },
  {
    path: "edit/:id",
    component: EditComponent,
    canActivate: [authGuard]
  },
  {
    path: "admin-dashboard",
    component: AdminDashboardComponent,
    canActivate: [adminGuard]
  },
  {
    path: "admin-comments/:id",
    component: AdminCommentsComponent,
    canActivate: [adminGuard]
  },
  {
    path: "**",
    redirectTo: "snippets"
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
