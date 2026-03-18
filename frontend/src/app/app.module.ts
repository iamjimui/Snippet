import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IonicModule } from '@ionic/angular';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './views/home/home.component';
import { NotificationComponent } from './components/navbar/notification/notification.component';
import { ProfilComponent } from './components/navbar/profil/profil.component';
import { SideMenuComponent } from './components/side-menu/side-menu.component';
import { HighlightModule, HIGHLIGHT_OPTIONS, HighlightOptions } from 'ngx-highlightjs';
import { SnippetCardComponent } from './components/snippet-card/snippet-card.component';
import { PickerComponent } from '@ctrl/ngx-emoji-mart';
import { LanguagesFilterPipe } from './pipes/languages-filter.pipe';
import { TagsFilterPipe } from './pipes/tags-filter.pipe';
import { LanguageFlagComponent } from './components/language-flag/language-flag.component';
import { SnippetCardHeaderComponent } from './components/snippet-card/snippet-card-header/snippet-card-header.component';
import { SnippetCardTitleComponent } from './components/snippet-card/snippet-card-title/snippet-card-title.component';
import { SnippetCardBodyComponent } from './components/snippet-card/snippet-card-body/snippet-card-body.component';
import { SnippetCardDescriptionComponent } from './components/snippet-card/snippet-card-description/snippet-card-description.component';
import { SnippetCardCodeComponent } from './components/snippet-card/snippet-card-code/snippet-card-code.component';
import { SnippetCardTagsComponent } from './components/snippet-card/snippet-card-tags/snippet-card-tags.component';
import { SnippetCardFooterComponent } from './components/snippet-card/snippet-card-footer/snippet-card-footer.component';
import { DetailsComponent } from './views/details/details.component';
import { AddComponent } from './views/add/add.component';
import { IosSwitchComponent } from './components/ios-switch/ios-switch.component';
import { LoaderComponent } from './components/loader/loader.component';
import { SnippetFilerPipe } from './pipes/snippet-filer.pipe';
import { PaginationComponent } from './components/pagination/pagination.component';
import { CommentsComponent } from './views/details/comments/comments.component';
import { FavoriteButtonComponent } from './components/favorite-button/favorite-button.component';
import { CopyButtonComponent } from './components/copy-button/copy-button.component';
import { OrderByPipe } from './pipes/order-by.pipe';
import { EditComponent } from './views/edit/edit.component';
import { AddTagDropdownComponent } from './components/add-tag-dropdown/add-tag-dropdown.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { RegisterComponent } from './views/register/register.component';
import { LoginComponent } from './views/login/login.component';
import { ReqInterceptor } from './interceptors/req.interceptor';
import { AdminDashboardComponent } from './views/admin-dashboard/admin-dashboard.component';
import { AdminCommentsComponent } from './views/admin-comments/admin-comments.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    NotificationComponent,
    ProfilComponent,
    SideMenuComponent,
    SnippetCardComponent,
    LanguagesFilterPipe,
    TagsFilterPipe,
    LanguageFlagComponent,
    SnippetCardHeaderComponent,
    SnippetCardTitleComponent,
    SnippetCardBodyComponent,
    SnippetCardDescriptionComponent,
    SnippetCardCodeComponent,
    SnippetCardTagsComponent,
    SnippetCardFooterComponent,
    DetailsComponent,
    AddComponent,
    IosSwitchComponent,
    LoaderComponent,
    SnippetFilerPipe,
    PaginationComponent,
    CommentsComponent,
    FavoriteButtonComponent,
    CopyButtonComponent,
    OrderByPipe,
    EditComponent,
    AddTagDropdownComponent,
    RegisterComponent,
    LoginComponent,
    AdminDashboardComponent,
    AdminCommentsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    IonicModule.forRoot(),
    HighlightModule,
    PickerComponent,
    FormsModule,
    HttpClientModule,
    CommonModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
  ],
  providers: [
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: <HighlightOptions>{
        fullLibraryLoader: () => import('highlight.js'),
      }
    },
    OrderByPipe,
    { provide: HTTP_INTERCEPTORS, useClass: ReqInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
