package com.LBA.LBAResource;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.LBA.category.CategoriesResource;
import com.LBA.category.CategoryResource;
import com.LBA.service.advertisement.AdvertisementResource;
import com.LBA.service.advertisement.AdvertisementsByDistanceBySubscriptionResource;
import com.LBA.service.advertisement.AdvertisementsByMerchantByDistanceResource;
import com.LBA.service.advertisement.AdvertisementsByMerchantIdResource;
import com.LBA.service.advertisement.AdvertisementsByMerchantResource;
import com.LBA.service.advertisement.AdvertisementsByProductResource;
import com.LBA.service.advertisement.AdvertisementsResource;
import com.LBA.service.channel.ChannelResource;
import com.LBA.service.channel.ChannelsByCategoryResource;
import com.LBA.service.channel.ChannelsByNameByUserResource;
import com.LBA.service.channel.ChannelsByNameResource;
import com.LBA.service.channel.ChannelsByUserNameResource;
import com.LBA.service.channel.ChannelsResource;
import com.LBA.service.channelSubscription.UserSubscriptionResource;
import com.LBA.service.channelSubscription.UserSubscriptionsByChannelNameResource;
import com.LBA.service.channelSubscription.UserSubscriptionsResource;
import com.LBA.service.mobileuser.MobileUserResource;
import com.LBA.service.mobileuser.MobileUserVerificationResource;
import com.LBA.service.mobileuser.MobileUsersResource;
import com.LBA.service.product.ProductResource;
import com.LBA.service.product.ProductsByChannelResource;
import com.LBA.service.product.ProductsByNameResource;
import com.LBA.service.product.ProductsResource;

/**
 * @author payalpatel
 * 
 */
public class LBAResourceApplication extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public Restlet createInboundRoot() {
		// Create a router Restlet that defines routes.
		Router router = new Router(getContext());

		router.attachDefault(ProductsResource.class);

		// Defines a route for the resource "list of products"
		router.attach("/products", ProductsResource.class);
		// Defines a route for the resource "product"
		router.attach("/products/{productId}", ProductResource.class);
		// Defines a route for the resource "products"
		router.attach("/products/channels/{channelId}",
				ProductsByChannelResource.class);
		// Defines a route for the resource "products"
		router.attach("/products/productname/{productName}",
				ProductsByNameResource.class);

		// Defines a route for the resource "list of channels"
		router.attach("/channels", ChannelsResource.class);
		// Defines a route for the resource "channel"
		router.attach("/channels/{channelId}", ChannelResource.class);
		// Defines a route for the resource "channelName"
		router.attach("/channels/channelname/{channelName}",
				ChannelsByNameResource.class);
		// Defines a route for the resource "channelBy!UserName"
		router.attach("/channels/username/{userName}",
				ChannelsByUserNameResource.class);
		// Defines a route for the resource "channelBy!UserName"
		router.attach(
				"/channels/channelname/{channelName}/username/{userName}",
				ChannelsByNameByUserResource.class);
		// Defines a route for the resource "channelBy!UserName"
		router.attach("/channels/category/{categoryName}",
				ChannelsByCategoryResource.class);

		// Defines a route for the resource "list of advertisements"
		router.attach("/advertisements", AdvertisementsResource.class);
		// Defines a route for the resource "advertisement"
		router.attach("/advertisements/{adId}", AdvertisementResource.class);
		// Defines a route for the resource "advertisements"
		router.attach("/advertisements/products/{productId}",
				AdvertisementsByProductResource.class);

		// Defines a route for the resource "Advertisement"
		router.attach("/advertisements/merchant/{adName}",
				AdvertisementsByMerchantResource.class);
		// Defines a route for the resource "Advertisement"
		router.attach("/advertisements/merchantId/{adId}",
				AdvertisementsByMerchantIdResource.class);

		// Defines a route for the resource "Advertisement ALL"
		router.attach(
				"/advertisements/merchant/{adName}/{latitude}/{longitude}",
				AdvertisementsByMerchantByDistanceResource.class);

		// Defines a route for the resource "Advertisement by Subscription"
		router.attach(
				"/advertisements/subscription/{username}/{latitude}/{longitude}",
				AdvertisementsByDistanceBySubscriptionResource.class);

		 // Defines a route for the resource "list of mobileusers"
        router.attach("/mobileusers", MobileUsersResource.class);
        // Defines a route for the resource "mobileuser"
        router.attach("/mobileusers/{username}", MobileUserResource.class);
        // Defines a route for the resource "mobileuser verification"
        router.attach("/mobileusers/authenticate/{username}/{password}",
                        MobileUserVerificationResource.class);

		// Defines a route for the resource "usersubscriptions"
		router.attach("/subscription/{username}/{channelId}",
				UserSubscriptionResource.class);
		// Defines a route for the resource "usersubscriptions"
		router.attach("/subscription/{username}",
				UserSubscriptionsResource.class);
		// Defines a route for the resource "usersubscriptions by channelName"
		router.attach("/subscription/{username}/channelname/{channelname}",
				UserSubscriptionsByChannelNameResource.class);

		// Defines a route for the resource "list of Categories"
		router.attach("/categories", CategoriesResource.class);
		// Defines a route for the resource "category"
		router.attach("/categories/{categoryName}", CategoryResource.class);

		return router;
	}
}
